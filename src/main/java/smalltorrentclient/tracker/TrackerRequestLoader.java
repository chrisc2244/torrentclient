package smalltorrentclient.tracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Random;
import smalltorrentclient.metainfo.BencodingEncoder;
import smalltorrentclient.metainfo.TorrentInfo;

public class TrackerRequestLoader
{

	public static TrackerRequest createInitialTrackerRequest(TorrentInfo torrentInfo) throws NoSuchAlgorithmException
	{
		TrackerRequest trackerRequest = new TrackerRequest();

		trackerRequest.announce = torrentInfo.announce;
		trackerRequest.infoHash = calculateInfoHash(torrentInfo.infoMap);
		trackerRequest.peerId = calculatePeerId();
		trackerRequest.port = 6881;
		trackerRequest.uploaded = 0;
		trackerRequest.downloaded = 0;
		// TODO: this has to be from the torrentinfo length or something idk
		// trackerRequest.left = ;

		trackerRequest.event = "started";


		return trackerRequest;
	}

	private static byte[] calculatePeerId()
	{
		// I like the Azureues style from https://wiki.theory.org/BitTorrentSpecification
		byte[] peerId = new byte[20];

		peerId[0] = '-';

		// cc for chris client! :D
		peerId[1] = 'C';
		peerId[2] = 'C';

		// Idk how to do version number right now, and it's not that important
		peerId[3] = '0';
		peerId[4] = '0';
		peerId[5] = '0';
		peerId[6] = '1';

		peerId[7] = '-';

		Random random = new Random();
		for (int i = 8; i < peerId.length; i++)
		{
			peerId[i] = (byte) random.nextInt(256);
		}


		return peerId;

	}


	private static byte[] calculateInfoHash(LinkedHashMap<String, Object> infoMap) throws NoSuchAlgorithmException
	{

		BencodingEncoder bencodingEncoder = new BencodingEncoder();

		byte[] encodedMap = bencodingEncoder.encode(infoMap);

		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

		return sha1.digest(encodedMap);
	}





}

