package smalltorrentclient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import smalltorrentclient.metainfo.TorrentInfo;
import static smalltorrentclient.metainfo.TorrentLoader.createTorrentInfo;
import smalltorrentclient.tracker.TrackerRequest;
import static smalltorrentclient.tracker.TrackerRequestLoader.createInitialTrackerRequest;
import smalltorrentclient.tracker.TrackerRequester;
import smalltorrentclient.tracker.TrackerResponse;

public class Main
{

	public static void main(String[] args) throws NoSuchAlgorithmException, URISyntaxException, IOException
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



		// load the torrent file
		TorrentInfo torrentInfo = createTorrentInfo("debian-12.8.0-amd64-netinst.iso.torrent");
		System.out.println("Printing torrentinfo: " + torrentInfo);

		// get the info needed to send initial tracker request
		TrackerRequest initialTrackerRequest = createInitialTrackerRequest(torrentInfo);
		System.out.println("Printing initial trackerRequest: " + initialTrackerRequest);

		// send the initial tracker request
		TrackerRequester trackerRequester = new TrackerRequester();
		TrackerResponse trackerResponse = trackerRequester.fetchTrackerResponse(initialTrackerRequest);

		System.out.println("Printing trackerResponse: " + trackerResponse);








	}
}
