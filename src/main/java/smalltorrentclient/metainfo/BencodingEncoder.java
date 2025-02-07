package smalltorrentclient.metainfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BencodingEncoder
{


	public byte[] encode(Object input)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encode(input, baos);
		return baos.toByteArray();
	}


	private static void encode(Object input, ByteArrayOutputStream baos)
	{
		try
		{
			if (input instanceof String)
			{
				byte[] strBytes = ((String) input).getBytes(StandardCharsets.UTF_8);
				baos.write(Integer.toString(strBytes.length).getBytes(StandardCharsets.US_ASCII));
				baos.write(':');
				baos.write(strBytes);

			}
			else if (input instanceof byte[] rawBytes)
			{
				baos.write(Integer.toString(rawBytes.length).getBytes(StandardCharsets.US_ASCII));
				baos.write(':');
				baos.write(rawBytes);
			}
			else if (input instanceof Number)
			{
				baos.write('i');
				baos.write(input.toString().getBytes(StandardCharsets.US_ASCII));
				baos.write('e');
			}
			else if (input instanceof List)
			{
				baos.write('l');
				for (Object o : (List<?>) input)
				{
					encode(o, baos);
				}
				baos.write('e');
			}
			else if (input instanceof Map<?, ?> map)
			{
				baos.write('d');
				List<String> keys = new ArrayList<>();
				for (Object key : map.keySet())
				{
					keys.add((String) key);
				}
				Collections.sort(keys);
				for (String key : keys)
				{
					encode(key, baos);
					encode(map.get(key), baos);
				}
				baos.write('e');
			}
			else
			{
				throw new IllegalArgumentException("Unsupported type: " + input.getClass().getName());
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("Error in Bencode encoding", e);
		}

	}
}
