package smalltorrentclient.metainfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TorrentLoader
{

	//	private byte[] load(String filename) throws IOException
//	{
//		byte[] fileContent;
//		try
//		{
//			fileContent = Files.readAllBytes(Paths.get(filename));
//		}
//		catch (IOException e)
//		{
//			System.out.println("Unable to open torrent file.");
//			throw new IOException(e);
//		}
//		return fileContent;
//	}
//
	byte[] loadFromResource(String filename)
	{
		byte[] fileContent;

		try
		{
			java.net.URL resourceUrl = getClass().getClassLoader().getResource(filename);
			System.out.println("Resource found at: " + resourceUrl);
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

			fileContent = inputStream.readAllBytes();
			System.out.println("Torrent file is: " + fileContent.length + " bytes in size");
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to load torrent file", e);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to decode torrent file", e);
		}
		return fileContent;
	}

	public static TorrentInfo createTorrentInfo(String filename)
	{
		TorrentLoader loader = new TorrentLoader();
		BencodingDecoder decoder = new BencodingDecoder();

		byte[] fileBytes = loader.loadFromResource(filename);
		LinkedHashMap<String, Object> decodedMap = decoder.decode(fileBytes);

		System.out.println("Print of decodedMap: " + decodedMap);

		TorrentInfo torrent = new TorrentInfo();

		Object announce = decodedMap.get("announce");
		if (announce instanceof String)
		{
			torrent.announce = (String) announce;
			System.out.println("Announce located: " + torrent.announce);
		}

		Object announceList = decodedMap.get("announce-list");
		if (announceList instanceof ArrayList)
		{
			torrent.announceList = parseAnnounceList((ArrayList<Object>) announceList);
			System.out.println("Announce list located: " + torrent.announceList);
		}

		Object creationDate = decodedMap.get("creation date");
		if (creationDate instanceof Long)
		{
			torrent.creationDate = (Long) creationDate;
			System.out.println("Creation date located: " + torrent.creationDate);
		}

		Object comment = decodedMap.get("comment");
		if (comment instanceof String)
		{
			torrent.comment = (String) comment;
			System.out.println("Comment located: " + torrent.comment);
		}

		Object createdBy = decodedMap.get("created by");
		if (createdBy instanceof String)
		{
			torrent.createdBy = (String) createdBy;
			System.out.println("Created by located: " + torrent.createdBy);
		}

		Object encoding = decodedMap.get("encoding");
		if (encoding instanceof String)
		{
			torrent.encoding = (String) encoding;
			System.out.println("Encoding located: " + torrent.encoding);
		}

		LinkedHashMap<String, Object> infoMap = (LinkedHashMap<String, Object>) decodedMap.get("info");
		torrent.infoMap = infoMap;
		parseInfoMap(infoMap, torrent);

		return torrent;
	}

	private static ArrayList<ArrayList<String>> parseAnnounceList(ArrayList<Object> announceList)
	{

		ArrayList<ArrayList<String>> parsedAnnounceList = new ArrayList<>();

		for (Object tierObj : announceList)
		{
			if (tierObj instanceof ArrayList)
			{
				ArrayList<Object> tierList = (ArrayList<Object>) tierObj;
				ArrayList<String> tier = new ArrayList<>();

				for (Object tracker : tierList)
				{
					if (tracker instanceof String)
					{
						tier.add((String) tracker);
					}
				}
				parsedAnnounceList.add(tier);
			}
		}
		return parsedAnnounceList;
	}

	private static void parseInfoMap(LinkedHashMap<String, Object> infoMap, TorrentInfo torrent)
	{
		// If this is here then it's multi file
		Object files = infoMap.get("fileInfos");

		// If this is here then it's single file
		Object length = infoMap.get("length");

		if (files instanceof ArrayList)
		{
			// multi file mode
			torrent.singleFile = false;
			torrent.multiFileInfo = new TorrentInfo().multiFileInfo;

			Object pieceLength = infoMap.get("piece length");
			if (pieceLength instanceof Long)
			{
				torrent.multiFileInfo.pieceLength = (Long) pieceLength;
			}

			Object name = infoMap.get("name");
			if (name instanceof String)
			{
				torrent.multiFileInfo.name = (String) name;
			}

			Object privacy = infoMap.get("private");

			if (privacy instanceof Long)
			{
				// because it will be stored as i0e or i1e
				torrent.multiFileInfo.privacy = ((Long) privacy == 1L);
			}

			Object piecesBytes = infoMap.get("pieces");
			if (piecesBytes instanceof byte[] piecesBytesArr)
			{
				torrent.multiFileInfo.pieceHashes = splitPieces(piecesBytesArr);
			}

			ArrayList<Object> filesList = (ArrayList<Object>) files;
			ArrayList<FileInfo> parsedFiles = new ArrayList<>();
			for (Object fileObj : filesList)
			{
				if (fileObj instanceof LinkedHashMap)
				{
					LinkedHashMap<String, Object> fileInfo = (LinkedHashMap<String, Object>) fileObj;
					parsedFiles.add(parseFileEntry(fileInfo));
				}
			}

			torrent.multiFileInfo.fileInfos = parsedFiles;
		}
		else if (length instanceof Long)
		{
			// single file mode
			torrent.singleFile = true;
			torrent.singleFileInfo = new SingleFileInfo();

			Object pieceLength = infoMap.get("piece length");
			if (pieceLength instanceof Long)
			{
				torrent.singleFileInfo.pieceLength = (Long) pieceLength;
			}

			Object name = infoMap.get("name");
			if (name instanceof String)
			{
				torrent.singleFileInfo.name = (String) name;
			}

			torrent.singleFileInfo.length = (Long) length;

			Object md5 = infoMap.get("md5sum");
			if (md5 instanceof String)
			{
				torrent.singleFileInfo.md5sum = (String) md5;
			}

			Object privacy = infoMap.get("private");
			if (privacy instanceof Long)
			{
				torrent.singleFileInfo.privacy = ((Long) privacy == 1L);
			}

			Object piecesBytes = infoMap.get("pieces");
			if (piecesBytes instanceof byte[] piecesBytesArr)
			{
				torrent.singleFileInfo.pieceHashes = splitPieces(piecesBytesArr);
			}
		}
		else
		{
			throw new RuntimeException("Couldn't determine single or multi file mode");
		}
	}

	private static FileInfo parseFileEntry(LinkedHashMap<String, Object> fileInfo)
	{

		FileInfo file = new FileInfo();

		Object length = fileInfo.get("length");
		if (length instanceof Long)
		{
			file.length = (Long) length;
		}

		Object path = fileInfo.get("path");
		if (path instanceof ArrayList)
		{
			ArrayList<Object> pathList = (ArrayList<Object>) path;
			ArrayList<String> pathSegments = new ArrayList<>();
			for (Object segment : pathList)
			{
				if (segment instanceof String)
				{
					pathSegments.add((String) segment);
				}
			}
			file.path = pathSegments;
		}

		Object md5 = fileInfo.get("md5sum");
		if (md5 instanceof String)
		{
			file.md5sum = (String) md5;
		}
		return file;
	}

	private static ArrayList<byte[]> splitPieces(byte[] piecesBytes)
	{
		final int hashSize = 20;
		ArrayList<byte[]> piecesHashes = new ArrayList<>();
		int count = piecesBytes.length / hashSize;

		for (int i = 0; i < count; i++)
		{
			byte[] singleHash = new byte[hashSize];
			System.arraycopy(piecesBytes, i * hashSize, singleHash, 0, hashSize);
			piecesHashes.add(singleHash);
		}
		return piecesHashes;
	}
}
