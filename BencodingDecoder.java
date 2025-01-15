public class BencodingDecoder
{

	// Take in a Byte array, leave loading the file and sending the byte array to another class
	// Iterate through the byte array, maybe decode to a map, something I can easily do next step from
	// Extract TorrentInfo fields
	// do what with this after I get it?

    /*
    i integers e, can be negative
    bytestring length:contents
    l listitems e
    d dictionarypairs e
     */


    /*
    iterate through the byte array
    if we encounter a character we recognize
    we call the decode method for that char
    then we read the number of bytes till the next :
    that tells us how many to iterate, and helps us avoind recognizing : in the middle of a field
    if we encounter


    for ints we can iterate through until we reach an e
    for lists we can iterate through
     */

	public static TorrentInfo decode(byte[] input) throws Exception
	{

		final TorrentInfo.TorrentInfoBuilder torrentInfo = new TorrentInfo.TorrentInfoBuilder();
		long counter = 0;

		for (byte b : input)
		{

			switch (b)
			{
				case 'i':
					DecodeInteger(counter);
					break;
				case 'l':
					DecodeList(counter);
					break;
				case 'd':
					DecodeDict(counter);
					break;
				default:
					throw new Exception("Valid ASCII char not detected.");

			}
		}
		return torrentInfo.build();
	}

	private static long DecodeDict(long counter)
	{
		System.out.println("IM INSIDE DECODEDICT" + counter);
		return counter;
	}

	private static long DecodeList(long counter)
	{
		System.out.println("IM INSIDE DECODELISTTTT" + counter);
		return counter;
	}

	private static long DecodeInteger(long counter)
	{
		System.out.println("IM INSIDE decodeint" + counter);
		return counter;
	}


}
