package smalltorrentclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;


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


	private static Object decodeObject(ListIterator<Byte> iterator)
	{
		if (!iterator.hasNext())
		{
			throw new IllegalArgumentException("Iterator is empty");
		}

		Byte b = iterator.next();


		switch (b)
		{
			case 'i':
				return decodeInteger(iterator);

			case 'l':
				return decodeList(iterator);

			case 'd':
				return decodeDict(iterator);

			default:
				if (Character.isDigit(b))
				{
					return decodeString(iterator, b);

				}
				else if (b == 'e')
				{
					throw new IllegalArgumentException("Unexpected 'e' byte, malformed torrent maybe");
				}
				else
				{
					throw new IllegalArgumentException("Unrecognized byte: " + b);
				}


		}
	}

	public static String decodeString(ListIterator<Byte> iterator, Byte b)

	{
		StringBuilder digitsOfStringLength = new StringBuilder();
		digitsOfStringLength.append(Character.getNumericValue(b));
		Byte currentByte = iterator.next();

		while (currentByte != ':')
		{
			int numericValue = Character.getNumericValue(currentByte);
			digitsOfStringLength.append(numericValue);
			currentByte = iterator.next();
		}

		int totalStringLength = Integer.parseInt(digitsOfStringLength.toString());

		StringBuilder finalString = new StringBuilder();
		for (int i = 0; i < totalStringLength; i++)
		{
			char characterValue = (char) iterator.next().byteValue();
			finalString.append(characterValue);
		}

		return finalString.toString();
	}

	private static ArrayList<Object> decodeList(ListIterator<Byte> iterator)
	{

		ArrayList<Object> list = new ArrayList<>();


		while (iterator.hasNext())
		{
			byte b = iterator.next();

			if (b == 'e')
			{
				break;
			}

			// Because now b is the first byte of a new object, but we already consumed it
			iterator.previous();

			list.add(decodeObject(iterator));
		}

		return list;

	}


	/*
	 According to https://wiki.theory.org/BitTorrentSpecification#Integers , "the maximum number of bit of this integer is unspecified,
	 but to handle it as a signed 64-bit integer is mandatory to handle "large files" aka .torrent for more than 4 GB"
	*/
	public static Long decodeInteger(ListIterator<Byte> iterator)
	{

		StringBuilder longBuilder = new StringBuilder();

		while (iterator.hasNext())
		{
			byte b = iterator.next();

			if (b == 'e')
			{
				break;
			}
			// Can't do Character.getNumericValue because we can have a negative symbol
			longBuilder.append(((char) b));
		}

		return Long.parseLong(longBuilder.toString());
	}

	private static LinkedHashMap<String, Object> decodeDict(ListIterator<Byte> iterator)
	{
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();

		while (iterator.hasNext())
		{
			byte b = iterator.next();

			if (b == 'e')
			{
				break;
			}

			String key = decodeString(iterator, b);
			Object value = decodeObject(iterator);

			map.put(key, value);
		}

		return map;
	}

	public static ListIterator<Byte> createIterator(byte[] bytes)
	{

		List<Byte> inputList = new ArrayList<>();
		for (byte b : bytes)
		{
			inputList.add(b);
		}
		return inputList.listIterator();

	}

	public TorrentInfo decode(byte[] input)
	{
		TorrentInfo torrentInfo = null;

		decodeObject(createIterator(input));

		return torrentInfo;
	}
}
