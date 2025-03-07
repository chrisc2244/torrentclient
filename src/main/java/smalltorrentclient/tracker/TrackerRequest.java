package smalltorrentclient.tracker;

import java.util.Arrays;

public class TrackerRequest

{

	public String announce;
	public byte[] infoHash;
	public byte[] peerId;
	public int port;
	public int uploaded;
	public int downloaded;
	public int left;

	// why yes... my client does in fact support this...
	public final boolean compactSupport = true;

	public boolean noPeerId;

	//not always specified
	public String event;
	//optional
	public String ip;
	//optional
	public int numWant;
	//optional
	public String key;
	//optional
	public String trackerId;


	@Override
	public String toString() {
		return "TrackerRequest{" +
				"announce='" + announce + '\'' +
				", infoHash=" + Arrays.toString(infoHash) +
				", peerId=" + Arrays.toString(peerId) +
				", port=" + port +
				", uploaded=" + uploaded +
				", downloaded=" + downloaded +
				", left=" + left +
				", compact=" + compactSupport +
				", noPeerId=" + noPeerId +
				", event=" + event +
				", ip='" + ip + '\'' +
				", numWant=" + numWant +
				", key='" + key + '\'' +
				", trackerId='" + trackerId + '\'' +
				'}';
	}
}



