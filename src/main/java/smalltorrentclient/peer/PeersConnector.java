package smalltorrentclient.peer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import smalltorrentclient.pieces.PieceManager;
import smalltorrentclient.tracker.TrackerRequest;
import smalltorrentclient.tracker.TrackerResponse;

public class PeersConnector
{
	byte[] infoHash;
	byte[] myPeerId;
	ArrayList<Peer> peers;
	PieceManager pieceManager;


	public PeersConnector(TrackerRequest trackerRequest, TrackerResponse trackerResponse, PieceManager pieceManager)
	{
		this.infoHash = trackerRequest.infoHash;
		this.myPeerId = trackerRequest.peerId;
		this.peers = trackerResponse.peersList;
		this.pieceManager = pieceManager;

		connectToAllPeers();

	}

	/*
	open a tcp socket to each peer in the list
	send the handshake
	get back peer's handshake and check infohash
	exchange bitfields/have messages
	send interested if i need pieces from them
	wait until they unchoke me
	do choking/unchoking states and prolly do rarest first piece selection
	get piece data and check sha1 of each piece
	continue until file is complete

	 */

	public void connectToAllPeers()
	{

		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor())
		{

			for (Peer peer : peers)
			{
				executor.submit(new PeerConnection(peer, infoHash, myPeerId, pieceManager));
			}
		}



	}

}
