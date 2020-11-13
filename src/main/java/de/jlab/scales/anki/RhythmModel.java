package de.jlab.scales.anki;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.Rhythm;

public class RhythmModel implements WithDifficulty, WithAssets {
  
  private final Rhythm rhythm;
  private String lilyString;
  private String lilyId;

  public RhythmModel(Rhythm rhythm) {
    this.rhythm = rhythm;
    this.lilyString = new LilyRhythm(rhythm).toLily();
    this.lilyId = Utils.assetId(lilyString);
  }

  @Override
  public int getDifficulty() {
    return rhythm.getDifficulty();
  }
  
  // TODO BEGIN move to new super class
  public String getPngName() {
    return format("%s.png", lilyId);
  }

  public String getMp3Name() {
    return format("%s.mp3",  lilyId);
  }
  
  public String getLilyName() {
    return format("%s.ly", lilyId);
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
  
  // TODO END move to new super class
}
