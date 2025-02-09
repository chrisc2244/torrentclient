package smalltorrentclient.tracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import static smalltorrentclient.tracker.TrackerResponseLoader.parseTrackerResponse;

public class TrackerRequester
{

	public static byte[] fetchRawTrackerResponse(TrackerRequest trackerRequest) throws URISyntaxException, IOException
	{
		String trackerUrl = buildTrackerUrl(trackerRequest);

		URL url = new URI(trackerUrl).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setUseCaches(false);

		int responseCode = connection.getResponseCode();
		if (!(responseCode == HttpURLConnection.HTTP_OK))
		{
			throw new IOException("HTTP status code: " + responseCode);

		}

		try (InputStream inputStream = connection.getInputStream())
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			// I have no idea if this is the right size
			byte[] temp = new byte[4096];
			int read;
			while ((read = inputStream.read(temp)) != -1)
			{
				buffer.write(temp, 0, read);
			}
			return buffer.toByteArray();
		}


	}

	public static String buildTrackerUrl(TrackerRequest trackerRequest)
	{

		/*
		http://some.tracker.com:999/announce
		?info_hash=12345678901234567890
		&peer_id=ABCDEFGHIJKLMNOPQRST
		&ip=255.255.255.255
		&port=6881
		&downloaded=1234
		&left=98765
		&event=stopped
		 */

		// im pretty sure it should never have a ? and we have to append... but just to be sure...
		StringBuilder sb = new StringBuilder(trackerRequest.announce);
		if (!trackerRequest.announce.contains("?"))
		{
			sb.append('?');
		}
		else
		{
			sb.append('&');
		}

		// I'm quite certain there is a framework or URIBuilder or something
		// I can use instead of doing this... too bad!
		String urlencodedInfoHash = urlEncodeByteArray(trackerRequest.infoHash);
		String urlencodedPeerId = urlEncodeByteArray(trackerRequest.peerId);

		sb.append("info_hash=").append(urlencodedInfoHash);
		sb.append("&peer_id=").append(urlencodedPeerId);
		sb.append("&port=").append(trackerRequest.port);
		sb.append("&uploaded=").append(trackerRequest.uploaded);
		sb.append("&downloaded=").append(trackerRequest.downloaded);
		sb.append("&left=").append(trackerRequest.left);
		sb.append("&compact=1");

		// must be one of started, completed, stopped if specified
		if (trackerRequest.event != null)
		{
			sb.append("&event=").append(trackerRequest.event);
		}

		String trackerUrl = sb.toString();
		System.out.println("Built tracker URL: " + trackerUrl);

		return trackerUrl;
	}

	private static String urlEncodeByteArray(byte[] array)
	{
		StringBuilder sb = new StringBuilder();

		for (byte b : array)
		{
			int unsignedByte = b & 0xFF;
			sb.append('%');
			sb.append(String.format("%02x", unsignedByte));
		}

		return sb.toString();
	}

	public TrackerResponse fetchTrackerResponse(TrackerRequest trackerRequest) throws URISyntaxException, IOException
	{
		byte[] rawResponse = fetchRawTrackerResponse(trackerRequest);

		return parseTrackerResponse(rawResponse);
	}
}
