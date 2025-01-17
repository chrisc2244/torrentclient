package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TorrentFileLoader
{


	public static byte[] load(String filename) throws IOException
	{
		byte[] fileContent;
		try
		{
			fileContent = Files.readAllBytes(Paths.get(filename));
		}
		catch (IOException e)
		{
			System.out.println("Unable to open torrent file.");
			throw new RuntimeException(e);
		}
		return fileContent;
	}
}
