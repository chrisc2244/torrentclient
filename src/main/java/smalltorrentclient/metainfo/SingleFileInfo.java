package smalltorrentclient.metainfo;

import java.util.ArrayList;


public class SingleFileInfo
{
	Long pieceLength;
	ArrayList<byte[]> pieceHashes;
	String name;
	Long length;
	String md5sum;
	Boolean privacy;

	@Override
	public String toString()
	{
		return "SingleFileInfo{" +
			"pieceLength=" + pieceLength +
			", pieceHashes=" + pieceHashes +
			", name='" + name + '\'' +
			", length=" + length +
			", md5sum='" + md5sum + '\'' +
			", privacy=" + privacy +
			'}';
	}
}

