import java.io.IOException;

public class TorrentClient
{

	public static void main(String[] args)
	{
		try
		{
			byte[] input = TorrentFileLoader.load("testtorrent.torrent");
			TorrentInfo torrentInfo = BencodingDecoder.decode(input);
			// Proceed with further processing...
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to load torrent file", e);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to decode torrent file", e);
		}
	}
}
