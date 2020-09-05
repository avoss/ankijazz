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
  private String id = Utils.uuid();
  private KeySignature keySignature;
  private int difficulty;
  private boolean bothNames;
  private boolean descending;

  public ScaleCard(Scale mode, KeySignature keySignature) {
    this(mode, keySignature, keySignature.getNumberOfAccidentals(), false);
  }
  
  public ScaleCard(Scale mode, KeySignature keySignature, int difficulty, boolean descending) {
    this.keySignature = keySignature;
    this.descending = descending;
    this.modeInfo = universe.info(mode);
    this.parentInfo = universe.info(modeInfo.getParent());
    this.difficulty = difficulty;
  }
  
  @Override
  public void setId(String id) {
   this.id = id;
  }
  
  public ScaleCard withBothNames() {
    this.bothNames = true;
    return this;
  }

  @Override
  public String[] getFields() {
    return new String[] { modeName(), direction(), modeTypeName(), modeRootName(), parentName(), parentTypeName(), parentRootName(), modePngName(), modeMp3Name() };
  }

  private String direction() {
    return descending ? "Descending" : "Ascending";
  }

  private String modeName() {
    if (bothNames) {
      return modeInfo.getScale().getRoot().getBothNames() + " " + modeInfo.getTypeName();
    }
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
    return format("<img src=\"%s.png\">", id);
  }

  private String modeMp3Name() {
    return format("[sound:%s.mp3]",  id);
  }

  public String lilyName() {
    return id + ".ly";
  }
  
  @Override
  public int getDifficulty() {
    return difficulty;
  }

  @Override
  public void writeAssets(Path dir) {
    try {
      Path path = dir.resolve(lilyName());
      String lily = new LilyScale(modeInfo, keySignature, modeName(), descending).toLily();
      Files.write(path, asList(lily));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
