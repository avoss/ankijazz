package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltInScaleTypes.DiminishedHalfWhole;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor7Pentatonic;
import static de.jlab.scales.theory.BuiltInScaleTypes.WholeTone;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class ScaleCard implements Card {
  // TODO duplicated in AnkiCards
  private static final ScaleUniverse universe = new ScaleUniverse(true, Major, MelodicMinor, HarmonicMinor, HarmonicMajor, DiminishedHalfWhole, WholeTone, Minor7Pentatonic, Minor6Pentatonic);
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private KeySignature keySignature;
  private int difficulty;
  private boolean descending;
  private String lilyString;
  private String lilyId;

  public ScaleCard(ScaleInfo modeInfo) {
    this(modeInfo, modeInfo.getKeySignature().getNumberOfAccidentals(), false);
  }
  
  public ScaleCard(ScaleInfo modeInfo, int difficulty, boolean descending) {
    this.modeInfo = modeInfo;
    this.keySignature = modeInfo.getKeySignature();
    this.descending = descending;
    this.parentInfo = modeInfo.getParentInfo();
    this.difficulty = difficulty;
    this.lilyString = new LilyScale(modeInfo, descending).toLily();
    this.lilyId = Utils.hash(lilyString);

  }
  
  @Override
  public String[] getFields() {
    return new String[] { modeName(), modeTypeName(), modeRootName(), parentName(), parentTypeName(), parentRootName(), modePngName(), modeMp3Name(), direction() };
  }

  private String direction() {
    return descending ? "Descending" : "Ascending";
  }

  private String modeName() {
    return modeInfo.getScaleName();
  }

  private String modeTypeName() {
    return modeInfo.getTypeName();
  }

  private String modeRootName() {
    return keySignature.notate(modeInfo.getScale().getRoot());
  }

  private String parentName() {
    return parentInfo.getScaleName();
  }

  private String parentTypeName() {
    return parentInfo.getTypeName();
  }

  private String parentRootName() {
    return keySignature.notate(parentInfo.getScale().getRoot());
  }

  private String modePngName() {
    return format("<img src=\"%s.png\">", lilyId);
  }

  private String modeMp3Name() {
    return format("[sound:%s.mp3]",  lilyId);
  }

  public String lilyName() {
    return format("%s.ly", lilyId);
  }
  
  @Override
  public int getDifficulty() {
    return difficulty;
  }

  @Override
  public void writeAssets(Path dir) {
    try {
      Path path = dir.resolve(lilyName());
      Files.write(path, asList(lilyString));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
