package org.example;

import java.io.IOException;

public class TorrentClient {

  public static void main(String[] args) {
    try {
      byte[] input = TorrentFileLoader.load("testtorrent.torrent");
      System.out.println(input.length + " bytes");
    } catch (IOException e) {
      throw new RuntimeException("Failed to load torrent file", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to decode torrent file", e);
    }
  }
}
