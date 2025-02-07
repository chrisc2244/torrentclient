package smalltorrentclient.metainfo;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BencodingEncoderTest
{


	@Test
	public void testEncode()
	{

		String filename = "debian-12.8.0-amd64-netinst.iso.torrent";
		TorrentLoader loader = new TorrentLoader();
		BencodingDecoder decoder = new BencodingDecoder();
		BencodingEncoder encoder = new BencodingEncoder();

		byte[] fileBytes = loader.loadFromResource(filename);
		LinkedHashMap<String, Object> decodedMap = decoder.decode(fileBytes);

		byte[] reencodedMap = encoder.encode(decodedMap);


		assertEquals(fileBytes.length, reencodedMap.length);

		for (int i = 0; i < fileBytes.length; i++)
		{
			assertEquals(fileBytes[i], reencodedMap[i]);
		}
	}
}