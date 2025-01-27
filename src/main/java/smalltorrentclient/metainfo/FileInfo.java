package smalltorrentclient.metainfo;

import java.util.ArrayList;

public class FileInfo
{
	Long length;
	ArrayList<String> path;
	String md5sum;

	@Override
	public String toString()
	{
		return "FileInfo{" +
			"length=" + length +
			", path=" + path +
			", md5sum='" + md5sum + '\'' +
			'}';
	}
}
