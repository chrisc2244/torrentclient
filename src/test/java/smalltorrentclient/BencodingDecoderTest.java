package smalltorrentclient;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static smalltorrentclient.BencodingDecoder.createIterator;
import static smalltorrentclient.BencodingDecoder.decodeInteger;
import static smalltorrentclient.BencodingDecoder.decodeString;

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
	public void testDecode()
	{
	}
}