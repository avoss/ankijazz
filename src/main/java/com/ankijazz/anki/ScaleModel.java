package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;

import de.jlab.scales.difficulty.Difficulties;
import de.jlab.scales.difficulty.WithDifficulty;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.lily.Direction;
import de.jlab.scales.lily.LilyScale;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.ScaleInfo;

public class ScaleModel implements WithDifficulty {
  private final ScaleInfo modeInfo;
  private final ScaleInfo parentInfo;
  private KeySignature keySignature;
  private Direction direction;
  private Clef clef;
  private Note instrument;
  private final double difficulty;
  
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
    this.difficulty = Difficulties.getScaleInfoDifficulty(modeInfo);
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

  @Override
  public double getDifficulty() {
    return difficulty;
  }
  
  public String getNotationKey() {
    return keySignature.getKeySignatureString();
  }

  public String getInstrument() {
    return instrument.getName(Accidental.FLAT);
  }
}
