package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.*;

public enum BuiltInScaleTypes implements ScaleType {
  Major(CMajor, "Major Scale", "Ionian", "Dorian", "Phrygian", "Lydian", "Mixolydian", "Aeolean", "Locrian"),
  MelodicMinor(CMelodicMinor, "Melodic Minor", "Melodic Minor", "Phrygian #6", "Lydian #5", "Lydian Dominant", "Aeolean #3", "Altered", "Ionian #1"),
  HarmonicMinor(CHarmonicMinor, "Harmonic Minor", "Harmonic Minor", "Locrian #6", "Ionian #5", "Dorian #4", "Phrygian Dominant", "Lydian #2", "Mixolydian #1"),
  HarmonicMajor(CHarmonicMajor, "Harmonic Major", "Ionian b6", "Dorian b5", "Phrygian b4", "Lydian b3", "Mixolydian b2", "Aeolean b1", "Locrian b7"),
  DiminishedHalfWhole(CDiminishedHalfWhole, "Diminished Half/Whole"),
  WholeTone(CWholeTone, "Whole Tone"),
  Minor7Pentatonic(CMinorPentatonic, "Minor Pentatonic"),
  Minor6Pentatonic(CMinor6Pentatonic, "Minor6 Pentatonic"),
  Major7(Cmaj7, "Δ7"),
  Major7Sharp11(Cmaj7Sharp11, "Δ7#11"),
  Minor7(Cm7, "-7"),
  Minor6(Cm6, "-6"),
  Dominant7(C7, "7"),
  Minor7b5(Cm7b5, "ø"),
  Diminished7(Cdim7, "o7"),
  MinorMajor7(Cmmaj7, "mΔ7"),
  Major7Sharp5(Cmaj7Sharp5, "Δ7#5"),
  Dominant7sus4(C7sus4, "7sus"),
  Dominant7flat9(C7flat9, "7b9"),
  Dominant7sharp9(C7sharp9, "7#9"),
  Dominant7flat5(C7flat5, "7b5"),
  Dominant7sharp5(C7sharp5, "7#5"),
  Dominant7flat5flat9(C7flat5flat9, "7b5b9"),
  Dominant7sharp5flat9(C7sharp5flat9, "7#5b9"),
  Dominant7flat5sharp9(C7flat5sharp9, "7b5#9"),
  Dominant7sharp5sharp9(C7sharp5sharp9, "7#5#9"),
  MajorTriad(CmajTriad, ""),
  MinorTriad(CminTriad, "m"),
  DiminishedTriad(CdimTriad, "o"),
  AugmentedTriad(CaugTriad, "+"),
  Sus4Triad(CsusTriad, "sus4");

  private Scale prototype;
  private String scaleName;
  private String[] modeNames;

  BuiltInScaleTypes(Scale prototype, String scaleName, String ... modeNames) {
    this.prototype = prototype;
    this.scaleName = scaleName;
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
  public String getScaleName() {
    return scaleName;
  }

}
