package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.lily.Direction;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.BuiltInScaleTypes;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.ScaleInfo;

public class ScaleModel {
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private KeySignature keySignature;
  private Direction direction;
  private Clef clef;
  private Note instrument;

  public ScaleModel(ScaleInfo info) {
    this(info, ASCENDING);
  }

  public ScaleModel(ScaleInfo info, Direction direction) {
    this(info, direction, Clef.TREBLE, Note.C);
  }
  
  public ScaleModel(ScaleInfo modeInfo, Direction direction, Clef clef, Note instrument) {
    this.modeInfo = modeInfo;
    this.direction = direction;
    this.clef = clef;
    this.instrument = instrument;
    this.keySignature = modeInfo.getKeySignature();
    this.parentInfo = modeInfo.getParentInfo();
  }

  public LilyScale getLilyScale() {
    return new LilyScale(modeInfo, direction, clef, instrument);
  }
  
  public String getClef() {
    return clef.getLabel();
  }

  public String getDirection() {
    return direction.getLabel();
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

  public int getDifficulty() {
    int difficulty = modeInfo.getKeySignature().getNumberOfAccidentals();
    difficulty += modeInfo.getScaleType() == BuiltInScaleTypes.Major ? 0 : 4;
    difficulty += modeInfo.isInversion() ? 3 : 0;
    return difficulty;
  }
  
  public String getNotationKey() {
    return keySignature.notationKey();
  }

  public String getInstrument() {
    return instrument.getName(Accidental.FLAT);
  }
}
