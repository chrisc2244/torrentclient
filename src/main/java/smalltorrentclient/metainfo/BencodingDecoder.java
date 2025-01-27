package smalltorrentclient.metainfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;


public class BencodingDecoder
{

	public static Object decodeObject(ListIterator<Byte> iterator)
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
					throw new IllegalArgumentException("Unexpected 'e' byte");
				}
				else
				{
					throw new IllegalArgumentException("Unrecognized byte: " + b + " at iterator position " + iterator.previousIndex());
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
	 but to handle it as a signed 64-bit integer is mandatory to handle "large fileInfos" aka .torrent for more than 4 GB"
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

			Object value;
			if (key.equals("pieces"))
			{
				value = decodeBytes(iterator);
			}
			else
			{
				value = decodeObject(iterator);
			}

			map.put(key, value);

		}

		return map;
	}

	private static byte[] decodeBytes(ListIterator<Byte> iterator)
	{
		StringBuilder lengthBuilder = new StringBuilder();
		while (iterator.hasNext())
		{
			byte currentByte = iterator.next();
			if (currentByte == ':')
			{
				break;
			}
			lengthBuilder.append((char) currentByte);
		}

		int totalStringLength = Integer.parseInt(lengthBuilder.toString());

		byte[] bytes = new byte[totalStringLength];
		for (int i = 0; i < totalStringLength; i++)
		{
			bytes[i] = iterator.next();
		}

		return bytes;
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


	public LinkedHashMap<String, Object> decode(byte[] input)
	{
		return new LinkedHashMap<>((LinkedHashMap<String, Object>) decodeObject(createIterator(input)));
	}
}
