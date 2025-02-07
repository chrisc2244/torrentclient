package smalltorrentclient.tracker;

import java.util.Map;

public class TrackerResponse
{

	public String failureReason;
	public String warningMessage;
	public int interval;
	public int minInterval;
	public String trackerId;
	public int complete;
	public int incomplete;
	//dictionary peers model
	public Map peersDict;
	//binary peers model
	public byte[] peersBinary;


}
