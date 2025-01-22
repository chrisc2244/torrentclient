package smalltorrentclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import static smalltorrentclient.BencodingDecoder.createIterator;
import static smalltorrentclient.BencodingDecoder.decodeObject;

public class TorrentFileLoader
{


	private static byte[] load(String filename) throws IOException
	{
		byte[] fileContent;
		try
		{
			fileContent = Files.readAllBytes(Paths.get(filename));
		}
		catch (IOException e)
		{
			System.out.println("Unable to open torrent file.");
			throw new IOException(e);
		}
		return fileContent;
	}

	private byte[] loadFromResource(String filename)
	{
		byte[] fileContent;

		try
		{
			java.net.URL resourceUrl = getClass().getClassLoader().getResource(filename);
			System.out.println("Resource found at: " + resourceUrl);
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

			fileContent = inputStream.readAllBytes();
			System.out.println(fileContent.length + " bytes");

			System.out.println(decodeObject(createIterator(fileContent)));

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


	private Torrent createTorrent()
	{
		Torrent torrent = new Torrent();
		TorrentFileLoader loader = new TorrentFileLoader();
		BencodingDecoder decoder = new BencodingDecoder();

		byte[] fileBytes = loader.loadFromResource("debian-12.8.0-amd64-netinst.iso.torrent");
		LinkedHashMap<String, Object> decodedMap = decoder.decode(fileBytes);




		return torrent;

	}
}
