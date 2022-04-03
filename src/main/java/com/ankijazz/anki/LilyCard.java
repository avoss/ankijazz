package com.ankijazz.anki;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.ankijazz.Utils;

public abstract class LilyCard implements Card {

  private String lilyString;
  private String assetId;

  protected LilyCard(String lilyString) {
    this.lilyString = lilyString;
    this.assetId = Utils.assetId(lilyString);
  }

  public String getPngName() {
    return format("%s.png", assetId);
  }

  public String getMp3Name() {
    return format("%s.mp3",  assetId);
  }

  public String getLilyName() {
    return format("%s.ly", assetId);
  }
  
  @Override
  public void writeAssets(Path dir) {
    try {
      Path path = dir.resolve(getLilyName());
      Files.write(path, asList(lilyString));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  @Override
  public String getAssetId() {
    return assetId;
  }

}
