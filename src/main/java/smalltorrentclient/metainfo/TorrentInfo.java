package smalltorrentclient.metainfo;

import java.util.ArrayList;
import java.util.Arrays;

public class TorrentInfo
{
    /*
    https://wiki.theory.org/BitTorrentSpecification#Metainfo_File_Structure
     */

	// Top level metainfo
	public String announce;
	public ArrayList<ArrayList<String>> announceList;
	public Long creationDate;
	public String comment;
	public String createdBy;
	public String encoding;

	// single file or multi-file type info
	public Boolean singleFile;
	public SingleFileInfo singleFileInfo;
	public MultiFileInfo multiFileInfo;

	public byte[] infoHash;

	@Override
	public String toString()
	{
		return "TorrentInfo{" +
			"announce='" + announce + '\'' +
			", announceList=" + announceList +
			", creationDate=" + creationDate +
			", comment='" + comment + '\'' +
			", createdBy='" + createdBy + '\'' +
			", encoding='" + encoding + '\'' +
			", singleFile=" + singleFile +
			", singleFileInfo=" + singleFileInfo +
			", multiFileInfo=" + multiFileInfo +
			", infoHash=" + Arrays.toString(infoHash) +
			'}';
	}
}


