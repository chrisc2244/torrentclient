package smalltorrentclient.metainfo;

import java.util.ArrayList;

public class MultiFileInfo
{
	Long pieceLength;
	ArrayList<byte[]> pieceHashes;
	String name;
	ArrayList<FileInfo> fileInfos;
	Boolean privacy;

	@Override
	public String toString()
	{
		return "MultiFileInfo{" +
			"pieceLength=" + pieceLength +
			", pieceHashes=" + pieceHashes +
			", name='" + name + '\'' +
			", fileInfos=" + fileInfos +
			", privacy=" + privacy +
			'}';
	}
}
