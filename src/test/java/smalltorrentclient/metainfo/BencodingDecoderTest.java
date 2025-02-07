package smalltorrentclient.metainfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static smalltorrentclient.metainfo.BencodingDecoder.createIterator;
import static smalltorrentclient.metainfo.BencodingDecoder.decodeInteger;
import static smalltorrentclient.metainfo.BencodingDecoder.decodeObject;
import static smalltorrentclient.metainfo.BencodingDecoder.decodeString;

public class BencodingDecoderTest
{

	@Test
	public void decode()
	{
	}

	@Test
	public void Get_String_From_Byte_Array()
	{
		byte[] testInput = {'8', ':', 'a', 'n', 'n', 'o', 'u', 'n', 'c', 'e'};
		List<Byte> byteList = new ArrayList<>();

		for (byte b : testInput)
		{
			byteList.add(b);

		}

		ListIterator<Byte> iterator = byteList.listIterator();
		Byte b = iterator.next();

		String answer = decodeString(iterator, b);
		assertEquals("announce", answer);

	}

	@Test
	public void Get_Integer_From_Byte_Array()
	{

		byte[] testInput = {'i', '8', '0', '4', '1', '2', '3', '4', '2', '0'};

		ListIterator<Byte> iterator = createIterator(testInput);
		iterator.next();

		long rightAnswer = 804123420;
		long answer = decodeInteger(iterator);

		assertEquals(rightAnswer, answer);
	}
	@Test
	public void testDecode() {
		String resourceName = "debian-12.8.0-amd64-netinst.iso.torrent";
		try {
			java.net.URL resourceUrl = getClass().getClassLoader().getResource(resourceName);
			assertNotNull(String.valueOf(resourceUrl), "Resource not found: " + resourceName);
			System.out.println("Resource found at: " + resourceUrl);

			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
			assertNotNull(inputStream.toString(), "Failed to load resource as InputStream: " + resourceName);

			byte[] input = inputStream.readAllBytes();
			System.out.println(input.length + " bytes");

			System.out.println(decodeObject(createIterator(input)));

		} catch (IOException e) {
			throw new RuntimeException("Failed to load torrent file", e);
		} catch (Exception e) {
			throw new RuntimeException("Failed to decode torrent file", e);
		}
	}

}