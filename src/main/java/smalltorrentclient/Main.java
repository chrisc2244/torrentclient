package smalltorrentclient;

import java.security.NoSuchAlgorithmException;
import smalltorrentclient.metainfo.TorrentInfo;
import static smalltorrentclient.metainfo.TorrentLoader.createTorrentInfo;
import smalltorrentclient.tracker.TrackerRequest;
import static smalltorrentclient.tracker.InitialTrackerRequestLoader.createInitialTrackerRequest;

public class Main
{

	public static void main(String[] args) throws NoSuchAlgorithmException
	{

	  /*
	  Load torrent
	  Parse it and fill out TorrentInfo class
	  Contact the tracker
	  Connect to peers and perform handshake
	  Message handling loop
	  Piece manager
	  Reassemble the files
	  Event loop
	   */

	  /*

	  open ui, wait for user to drag in torrent file
	  once torrent file is dragged in, start the download and show a progress bar and cancel button


	  torrentClient.start();

	   */



		TorrentInfo torrentInfo = createTorrentInfo("debian-12.8.0-amd64-netinst.iso.torrent");

		System.out.println("Printing torrentinfo: " + torrentInfo);

		TrackerRequest trackerRequest = createInitialTrackerRequest(torrentInfo);

		System.out.println("Printing initial trackerRequest: " + trackerRequest);






	}
}
