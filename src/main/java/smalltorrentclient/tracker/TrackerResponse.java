package smalltorrentclient.tracker;

import java.util.ArrayList;

public class TrackerResponse
{

	// only present if failed
	public String failureReason;

	// optional
	public String warningMessage;

	public Long interval;

	// optional
	public int minInterval;

	// I think... this is optional..?
	public String trackerId;

	public Long complete;
	public Long incomplete;

	public boolean compactPeers;
	public ArrayList<Peer> peersList;

	@Override
	public String toString()
	{
		return "TrackerResponse{" +
			"failureReason='" + failureReason + '\'' +
			", warningMessage='" + warningMessage + '\'' +
			", interval=" + interval +
			", minInterval=" + minInterval +
			", trackerId='" + trackerId + '\'' +
			", complete=" + complete +
			", incomplete=" + incomplete +
			", compactPeers=" + compactPeers +
			", peersList=" + peersList +
			'}';
	}
}


