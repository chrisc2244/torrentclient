package smalltorrentclient.tracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TrackerRequester
{


	public static byte[] fetchTrackerResponse(TrackerRequest trackerRequest) throws URISyntaxException, IOException
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

		String urlencodedInfoHash = urlEncodeByteArray(trackerRequest.infoHash);
		String urlencodedPeerId = urlEncodeByteArray(trackerRequest.peerId);

		// "http://tracker.example.com/announce?info_hash=%12AB...&peer_id=%CC..."
		//   + "&port=6881&uploaded=0&downloaded=0&left=12345&event=started"

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


		sb.append("info_hash=").append(urlencodedInfoHash);
		sb.append("&peer_id=").append(urlencodedPeerId);
		sb.append("&port=").append(trackerRequest.port);
		sb.append("&uploaded=").append(trackerRequest.uploaded);
		sb.append("&downloaded=").append(trackerRequest.downloaded);
		sb.append("&left=").append(trackerRequest.left);

		// must be one of started, completed, stopped if specified
		if (trackerRequest.event != null)
		{
			sb.append("&event=").append(trackerRequest.event);
		}

		return sb.toString();
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
}
