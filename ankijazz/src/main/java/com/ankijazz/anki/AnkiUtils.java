package com.ankijazz.anki;

public class AnkiUtils {
  private AnkiUtils() {}
  
  public static String ankiMp3(String mp3Name) {
    return String.format("[sound:%s]", mp3Name);
  }
  
  public static String ankiPng(String pngName) {
    return String.format("<img src=\"%s\">", pngName);
  }

}
