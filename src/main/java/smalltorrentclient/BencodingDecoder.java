package smalltorrentclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class BencodingDecoder
{

    /*
    i integers e, can be negative
    bytestring length:contents
    l listitems e
    d dictionarypairs e
     */


	/*



d
  8:announce            33:http://192.168.1.74:6969/announce
  7:comment             17:Comment goes here
  10:created by         25:Transmission/2.92 (14714)
  13:creation date      i1460444420e
  8:encoding            5:UTF-8
  4:info
    d
      6:length          i59616e
      4:name            9:lorem.txt
      12:piece length   i32768e
      6:pieces          40:L@fR���3�K*Ez�>_YS��86��"�&�p�<�6�C{�9G
      7:private         i0e
    e
e
	 */


		/*
		iterate through entire input
		create new string holder
		iterate through digits till we get to :, adding every digiti to a list
		concat the list of ints together to form one single int
		use that as the counter to iterate to the end of the string, save this as key
		then check if its a digit, if so, repeat above, now we have our 2 strings and we add them to our overall dict

		else check if its a d
		if so, create a new temp dict, we can pass this into decodestring as the dict to add to, can do this for everything, making it recursive
		continue iterating through following above string strategy, adding these new string pairs to the temp dict
		once we reach an e, we have now finished creating the temp dict and have our string object pair and can add to overall dict



		 */


	public TorrentInfo decode(byte[] input)
	{
		TorrentInfo torrentInfo = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		// bruh you can insert another dict by just using the object type. duh, info is the key and the other dict is the object.

		decodeHelper(input, map);

		// write some stuff here to fill out torrentInfo with map info
		// build torrent method
		// probably just searching for each key and filling it out

		return torrentInfo;
	}

	private static void decodeHelper(byte[] input, LinkedHashMap<String, Object> output)
	{

		Iterator<Byte> iterator = createIterator(input);

		while (iterator.hasNext())
		{
			Byte b = iterator.next();

			if (b == 'd')
			{
				DecodeDict(iterator, output);
			}
			else if (b == 'l')
			{
				DecodeList(iterator);

			}
			else if (b == 'i')
			{
				DecodeInteger(iterator);
			}
			else if (Character.isDigit(b))
			{
				DecodeString(iterator, b);
			}
			else
			{
				throw new IllegalArgumentException("Unrecognized character: " + b);
			}
		}
	}

	public static String DecodeString(Iterator<Byte> iterator, byte b)

	{


		StringBuilder buildNumber = new StringBuilder();
		byte current = iterator.next();

		while (current != ':')
		{
			buildNumber.append(current);
			current = iterator.next();
		}

		int totalBytesInString = Integer.parseInt(buildNumber.toString());

		StringBuilder buildString = new StringBuilder();
		for (int i = 0; i < totalBytesInString; i++)
		{
			buildString.append(iterator.next());
		}

		return buildString.toString();
	}


	private static void DecodeList(Iterator<Byte> iterator)
	{
		System.out.println("IM INSIDE DECODELISTTTT");
	}

	private static void DecodeInteger(Iterator<Byte> iterator)
	{
		System.out.println("IM INSIDE decodeint");
	}

	private static void DecodeDict(Iterator<Byte> iterator, LinkedHashMap<String, Object> map)
	{
		LinkedHashMap<String, Object> innerMap = new LinkedHashMap<>();


		System.out.println("IM INSIDE DECODEDICT");

	}

	private static Iterator<Byte> createIterator(byte[] bytes)
	{

		List<Byte> inputList = new ArrayList<>();
		for (byte b : bytes)
		{
			inputList.add(b);
		}
		return inputList.iterator();

	}
}
