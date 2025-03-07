package smalltorrentclient.metainfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
	public Boolean isSingleFile;
	public SingleFileInfo singleFileInfo;
	public MultiFileInfo multiFileInfo;

	// use this for re-bencoding to calculate infoHash in TrackerRequest
	public LinkedHashMap<String, Object> infoMap;


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
			", singleFile=" + isSingleFile +
			", singleFileInfo=" + singleFileInfo +
			", multiFileInfo=" + multiFileInfo +
			", infoMap=" + infoMap +
			'}';
	}
}


