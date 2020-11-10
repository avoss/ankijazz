package de.jlab.scales.anki;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.BuiltInScaleTypes;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.ScaleInfo;

public class ScaleModel implements WithDifficulty, WithAssets {
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private KeySignature keySignature;
  private boolean descending;
  private String lilyString;
  private String lilyId;

  public ScaleModel(ScaleInfo modeInfo, boolean descending) {
    this.modeInfo = modeInfo;
    this.keySignature = modeInfo.getKeySignature();
    this.descending = descending;
    this.parentInfo = modeInfo.getParentInfo();
    this.lilyString = new LilyScale(modeInfo, descending).toLily();
    this.lilyId = Utils.assetId(lilyString);
  }
  
  public String getDirection() {
    return descending ? "Descending" : "Ascending";
  }

  public String getModeName() {
    return modeInfo.getScaleName();
  }

  public String getModeTypeName() {
    return modeInfo.getTypeName();
  }

  public String getModeRootName() {
    return keySignature.notate(modeInfo.getScale().getRoot());
  }

  public String getParentName() {
    return parentInfo.getScaleName();
  }

  public String getParentTypeName() {
    return parentInfo.getTypeName();
  }

  public String getParentRootName() {
    return keySignature.notate(parentInfo.getScale().getRoot());
  }

  public String getModeAnkiPng() {
    return format("<img src=\"%s.png\">", lilyId);
  }

  public String getModeAnkiMp3() {
    return format("[sound:%s.mp3]",  lilyId);
  }

  public String getModeHtmlPng() {
    return format("%s.png", lilyId);
  }

  public String getModeHtmlMp3() {
    return format("%s.mp3",  lilyId);
  }
  
  public String getLilyName() {
    return format("%s.ly", lilyId);
  }
  
  @Override
  public int getDifficulty() {
    int difficulty = modeInfo.getKeySignature().getNumberOfAccidentals();
    difficulty += modeInfo.getScaleType() == BuiltInScaleTypes.Major ? 0 : 4;
    difficulty += modeInfo.isInversion() ? 3 : 0;
    return difficulty;
  }
  
  public String getNotationKey() {
    return keySignature.notationKey();
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
}
