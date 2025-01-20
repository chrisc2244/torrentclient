package smalltorrentclient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TorrentInfo
{
    /*
    https://wiki.theory.org/BitTorrentSpecification#Metainfo_File_Structure

    info
    single file mode:
        name
        length
        md5sum

    multiple file mode:
        name
        files
            length
            md5sum
            path

    announce
    announce-list
    creation date
    comment
    created by
    encoding

    ____info
    piece length
    pieces
    private
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
	public ArrayList<String> pieces = new ArrayList<>();
	public String privacy;

	public static TorrentInfo createTorrentInfo(LinkedHashMap<String, Object> decodedBytes)
	{
		TorrentInfo torrentInfo = new TorrentInfo();


		return torrentInfo;

	}

	public void setAnnounce(String announce)
	{
		this.announce = announce;
	}

	public void setAnnounceList(ArrayList<String> announceList)
	{
		this.announceList = announceList;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setCreationDate(String creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public void setLength(long length)
	{
		this.length = length;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPieceLength(long pieceLength)
	{
		this.pieceLength = pieceLength;
	}

	public void setPieces(ArrayList<String> pieces)
	{
		this.pieces = pieces;
	}

	public void setPrivacy(String privacy)
	{
		this.privacy = privacy;
	}
}

