package smalltorrentclient;

import java.util.ArrayList;

public class Torrent
{
    /*
    https://wiki.theory.org/BitTorrentSpecification#Metainfo_File_Structure
     */

	public String announce;
	public ArrayList<String> announceList = new ArrayList<>();
	public String creationDate;
	public String comment;
	public String createdBy;
	public String encoding;
	public long length;
	public String name;
	public long pieceLength;
	public ArrayList<String> pieceHashes = new ArrayList<>();
	public String privacy;

}

