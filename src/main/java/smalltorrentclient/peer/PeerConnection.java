package smalltorrentclient.peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import smalltorrentclient.pieces.PieceManager;

public class PeerConnection implements Runnable
{

	private final Peer peer;
	InputStream inputStream;
	OutputStream outputStream;
	// clients start as choked and not interested
	private boolean amChoking = true;
	private boolean amInterested = false;
	private boolean peerChoking = true;
	private boolean peerInterested = false;
	private byte[] infoHash;
	private byte[] myPeerId;
	private byte[] receivedPeerId;
	private PieceManager pieceManager;

	public PeerConnection(Peer peer, byte[] infoHash, byte[] myPeerId, PieceManager pieceManager)
	{
		this.peer = peer;
		this.infoHash = infoHash;
		this.myPeerId = myPeerId;
		this.pieceManager = pieceManager;
	}

	@Override
	public void run()
	{
		try (Socket socket = new Socket())
		{
			socket.connect(new InetSocketAddress(peer.ip, peer.port.intValue()));
			socket.setSoTimeout(30_000);

			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();

			sendHandshake();

			if (!readHandshake())
			{
				System.out.println("Handshake failed with peer: " + peer.ip);
				return;
			}

			System.out.println("Handshake successful with peer: " + peer.ip);

		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	public void sendHandshake() throws IOException
	{
		final byte pstrlen = 19;
		final byte[] pstr = "BitTorrent protocol".getBytes(StandardCharsets.UTF_8);
		final byte[] reserved = new byte[8];

		outputStream.write(pstrlen);
		outputStream.write(pstr);
		outputStream.write(reserved);
		outputStream.write(infoHash);
		outputStream.write(myPeerId);

	}

	public boolean readHandshake() throws IOException
	{
		byte[] pstrlen = inputStream.readNBytes(1);
		if (pstrlen.length < 1 || pstrlen[0] != 19)
		{
			System.out.println("Incorrect pstrlen received from peer: " + peer.ip);
			return false;
		}

		byte[] pstr = inputStream.readNBytes(19);
		String pstrStr = new String(pstr, StandardCharsets.UTF_8);
		if (!pstrStr.equals("BitTorrent protocol"))
		{
			System.out.println("Incorrect protocol received from peer: " + peer.ip);
			return false;
		}

		byte[] reserved = inputStream.readNBytes(8);
		byte[] receivedInfoHash = inputStream.readNBytes(20);
		this.receivedPeerId = inputStream.readNBytes(20);

		boolean infoHashMatches = Arrays.equals(infoHash, receivedInfoHash);
		if (!infoHashMatches)
		{
			System.out.println("Incorrect infoHash received from peer: " + peer.ip);
			return false;
		}

		return true;


	}

}



