package smalltorrentclient.tracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import smalltorrentclient.metainfo.BencodingDecoder;
import smalltorrentclient.peer.Peer;

public class TrackerResponseLoader
{


	public static TrackerResponse parseTrackerResponse(byte[] rawTrackerResponse) throws IOException
	{

		BencodingDecoder bencodingDecoder = new BencodingDecoder();
		TrackerResponse trackerResponse = new TrackerResponse();

		LinkedHashMap<String, Object> trackerResponseMap = bencodingDecoder.decode(rawTrackerResponse);


		Object failureReason = trackerResponseMap.get("failure reason");
		if (failureReason instanceof String)
		{
			trackerResponse.failureReason = (String) failureReason;
			throw new IOException("Tracker response shows error: " + failureReason);

		}

		Object warningMessage = trackerResponseMap.get("warning message");
		if (warningMessage instanceof String)
		{
			System.out.println("Warning message located:" + warningMessage);
			trackerResponse.warningMessage = (String) warningMessage;
		}

		Object interval = trackerResponseMap.get("interval");
		if (interval instanceof Long)
		{
			System.out.println("interval located:" + interval);
			trackerResponse.interval = (Long) interval;
		}

		Object minInterval = trackerResponseMap.get("min interval");
		if (minInterval instanceof Long)
		{
			System.out.println("minInterval located: " + minInterval);
			trackerResponse.interval = (Long) minInterval;
		}

		Object trackerId = trackerResponseMap.get("tracker id");
		if (trackerId instanceof String)
		{
			System.out.println("trackerId located: " + trackerId);
			trackerResponse.trackerId = (String) trackerId;
		}

		Object complete = trackerResponseMap.get("complete");
		if (complete instanceof Long)
		{
			System.out.println("complete located: " + complete);
			trackerResponse.complete = (Long) complete;
		}

		Object incomplete = trackerResponseMap.get("incomplete");
		if (incomplete instanceof Long)
		{
			System.out.println("incomplete located: " + incomplete);
			trackerResponse.incomplete = (Long) incomplete;
		}

		Object peers = trackerResponseMap.get("peers");
		if (peers instanceof ArrayList)
		{
			System.out.println("Detected non-compact peer list");
			trackerResponse.compactPeers = false;
			trackerResponse.peersList = parsePeersList((ArrayList<LinkedHashMap<String, Object>>) peers);
		}
		else if (peers instanceof byte[])
		{
			System.out.println("Detected compact peer list");
			trackerResponse.compactPeers = true;
			trackerResponse.peersList = parsePeersString((byte[]) peers);
		}
		else
		{
			throw new IOException("Unable to parse peers from rawTrackerResponse");

		}

		return trackerResponse;
	}


	private static ArrayList<Peer> parsePeersList(ArrayList<LinkedHashMap<String, Object>> peers) throws IOException
	{

		ArrayList<Peer> peersList = new ArrayList<>();

		if (peers.isEmpty())
		{
			throw new IOException("No peers in rawTrackerResponse, no one is seeding possibly...?");
		}

		for (LinkedHashMap<String, Object> peer : peers)
		{
			Peer newPeer = new Peer();

			Object peerId = peer.get("peer id");
			if (peerId instanceof String)
			{
				newPeer.peerId = (String) peerId;
			}

			Object ip = peer.get("ip");
			if (ip instanceof String)
			{
				newPeer.ip = (String) ip;
			}
			else
			{
				throw new IOException("Unable to locate peer ip for peer:" + peer);
			}

			Object port = peer.get("port");
			if (port instanceof Long)
			{
				newPeer.port = (Long) port;
			}
			else
			{
				throw new IOException("Unable to locate port for peer:" + peer);
			}

			peersList.add(newPeer);
		}

		return peersList;
	}

	private static ArrayList<Peer> parsePeersString(byte[] peers) throws IOException
	{
		ArrayList<Peer> peersList = new ArrayList<>();

		if (peers.length % 6 != 0)
		{
			throw new IOException("peers byte[] not multiple of 6, malformed compact format");
		}

		for (int i = 0; i < peers.length; i += 6)
		{
			int ip1 = peers[i] & 0xFF;
			int ip2 = peers[i + 1] & 0xFF;
			int ip3 = peers[i + 2] & 0xFF;
			int ip4 = peers[i + 3] & 0xFF;
			String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;

			int port = ((peers[i+4] & 0xFF) << 8) | (peers[i+5] & 0xFF);

			Peer newPeer = new Peer();
			newPeer.ip = ip;
			newPeer.port = (long) port;

			peersList.add(newPeer);
		}

		return peersList;

	}


}



