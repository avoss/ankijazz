package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat5;
import static de.jlab.scales.theory.Scales.C7flat5flat9;
import static de.jlab.scales.theory.Scales.C7flat5sharp9;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C7sharp5flat9;
import static de.jlab.scales.theory.Scales.C7sharp5sharp9;
import static de.jlab.scales.theory.Scales.C7sharp9;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMajor;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinor6Pentatonic;
import static de.jlab.scales.theory.Scales.CMinorPentatonic;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static de.jlab.scales.theory.Scales.CaugTriad;
import static de.jlab.scales.theory.Scales.Cdim7;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.Cm6;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp11;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp5;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.CminTriad;
import static de.jlab.scales.theory.Scales.Cmmaj7;
import static de.jlab.scales.theory.Scales.CsusTriad;

import java.util.function.Function;


public enum BuiltInScaleTypes implements ScaleType {
  Major(CMajor, "Major Scale", C, "Major Scale", "Dorian", "Phrygian", "Lydian", "Mixolydian", "Aeolean", "Locrian"),
  MelodicMinor(CMelodicMinor,  "Melodic Minor", D, "Melodic Minor", "Dorian b2", "Lydian Augmented", "Lydian Dominant", "Mixolydian b6", "Locrian natural 2", "Altered"),
  HarmonicMinor(CHarmonicMinor, "Harmonic Minor", A, "Harmonic Minor", "Locrian #6", "Ionian #5", "Dorian #4", "Phrygian Dominant", "Lydian #2", "Mixolydian #1"),
  HarmonicMajor(CHarmonicMajor, "Harmonic Major", C, "Harmonic Major", "Dorian b5", "Phrygian b4", "Lydian b3", "Mixolydian b2", "Aeolean b1", "Locrian b7"),

  DiminishedHalfWhole(CDiminishedHalfWhole, "Diminished Half/Whole", "Diminished Half/Whole", "Diminished Whole/Half"),
  WholeTone(CWholeTone, "Whole Tone"),
  
  Minor7Pentatonic(CMinorPentatonic, "Minor Pentatonic", A, "Minor Pentatonic", "Major Pentatonic"),
  Minor6Pentatonic(CMinor6Pentatonic, "Minor6 Pentatonic", D, "Minor6 Pentatonic", null, "Dominant7 Pentatonic", null, "Minor7b5 Pentatonic"),
  
  Major7(Cmaj7, "Δ7", C),
  Major7Sharp11(Cmaj7Sharp11, "Δ7#11", C),
  Minor7(Cm7, "-7", A),
  Minor6(Cm6, "-6", D),
  Dominant7(C7, "7", G),
  Minor7b5(Cm7b5, "ø", B),
  Diminished7(Cdim7, "o7", G),
  MinorMajor7(Cmmaj7, "mΔ7", A),
  Major7Sharp5(Cmaj7Sharp5, "Δ7#5", C),
  Dominant7sus4(C7sus4, "7sus", G),
  Dominant7flat9(C7flat9, "7b9", G),
  Dominant7sharp9(C7sharp9, "7#9", G),
  Dominant7flat5(C7flat5, "7b5", G), // TODO #11?
  Dominant7sharp5(C7sharp5, "7#5", G), // TODO b13?
  Dominant7flat5flat9(C7flat5flat9, "7b5b9", G),
  Dominant7sharp5flat9(C7sharp5flat9, "7#5b9", G),
  Dominant7flat5sharp9(C7flat5sharp9, "7b5#9", G),
  Dominant7sharp5sharp9(C7sharp5sharp9, "7#5#9", G),
  
  MajorTriad(CmajTriad, "", C),
  MinorTriad(CminTriad, "m", A),
  DiminishedTriad(CdimTriad, "o", B),
  AugmentedTriad(CaugTriad, "+", G),
  Sus4Triad(CsusTriad, "sus4", G)
  ;
  
  private final Scale prototype;
  private final String typeName;
  private final String[] modeNames;
  private final Function<Note, Note> notationKey;
  
  BuiltInScaleTypes(Scale prototype, String typeName, Note modeKey, String ... modeNames) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.notationKey = (n) -> n.transpose(-modeKey.ordinal());
    this.modeNames = modeNames;
  }

  BuiltInScaleTypes(Scale prototype, String typeName, String ... modeNames) {
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

  @Override
  public Function<Note, Note> notationKey() {
    return notationKey;
  }

}
