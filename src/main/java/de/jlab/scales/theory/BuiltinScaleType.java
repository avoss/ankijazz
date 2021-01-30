package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMajor;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinor6Pentatonic;
import static de.jlab.scales.theory.Scales.CMinor7Pentatonic;
import static de.jlab.scales.theory.Scales.CWholeTone;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;


public enum BuiltinScaleType implements ScaleType {
  Major(CMajor, "Major Scale", C, "Major Scale", "Dorian", "Phrygian", "Lydian", "Mixolydian", "Aeolean", "Locrian"),
  MelodicMinor(CMelodicMinor,  "Melodic Minor", D, "Melodic Minor", "Dorian b2", "Lydian Augmented", "Lydian Dominant", "Mixolydian b6", "Locrian natural 2", "Altered"),
  HarmonicMinor(CHarmonicMinor, "Harmonic Minor", A, "Harmonic Minor", "Locrian #6", "Ionian #5", "Dorian #4", "Phrygian Dominant", "Lydian #2", "Mixolydian #1"),
  HarmonicMajor(CHarmonicMajor, "Harmonic Major", C, "Harmonic Major", "Dorian b5", "Phrygian b4", "Lydian b3", "Mixolydian b2", "Aeolean b1", "Locrian b7"),

  DiminishedHalfWhole(CDiminishedHalfWhole, "Diminished Half/Whole", "Diminished Half/Whole", "Diminished Whole/Half"),
  WholeTone(CWholeTone, "Whole Tone"),
  
  Minor7Pentatonic(CMinor7Pentatonic, "Minor Pentatonic", A, "Minor Pentatonic", "Major Pentatonic"),
  Minor6Pentatonic(CMinor6Pentatonic, "Minor6 Pentatonic", D, "Minor6 Pentatonic", null, "Dominant7 Pentatonic", null, "Minor7b5 Pentatonic"),
  ;
  
  private final Scale prototype;
  private final String typeName;
  private final String[] modeNames;
  private final Function<Note, Note> notationKey;
  
  BuiltinScaleType(Scale prototype, String typeName, Note modeKey, String ... modeNames) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.notationKey = (n) -> n.transpose(-modeKey.ordinal());
    this.modeNames = modeNames;
  }

  BuiltinScaleType(Scale prototype, String typeName, String ... modeNames) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.notationKey = (n) -> Note.C;
    this.modeNames = modeNames;
  }
  
  @Override
  public Scale getPrototype() {
    return prototype;
  }

  @Override
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  Function<Note, Note> notationKey() {
    return notationKey;
  }

  @Override
  public boolean isChord() {
    return false;
  }

  @Override
  public Set<KeySignature> getKeySignatures(Note root) {
    Note notationKey = notationKey().apply(root);
    Accidental accidental = Accidental.preferredAccidentalForMajorKey(notationKey);
    Set<KeySignature> result = new LinkedHashSet<>();
    KeySignature keySignature = KeySignature.fromScale(prototype.transpose(root), notationKey, accidental);
    result.add(keySignature);
    if (notationKey == Note.Gb) {
      result.add(KeySignature.fromScale(prototype.transpose(root), notationKey, accidental.inverse()));
    }
    return result;
  }
}
