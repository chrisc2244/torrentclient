package smalltorrentclient.peer;

public class Peer
{

	// I guess this is optional, the bittorrent spec doesn't say it is...
	// but when I do a test request on the debian tracker im not getting one, so...
	public String peerId;

	public String ip;
	public Long port;


	@Override
	public String toString()
	{
		return "Peer{" +
			"peerId='" + peerId + '\'' +
			", ip='" + ip + '\'' +
			", port=" + port +
			'}';
	}
}
