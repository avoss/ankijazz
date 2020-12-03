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
import static de.jlab.scales.theory.Scales.Csus4Triad;

import java.util.function.Function;


public enum BuiltInChordTypes implements ScaleType {
  Major7(Cmaj7, "Δ7", C),
  Major7Sharp11(Cmaj7Sharp11, "Δ7#11", C),
  Minor7(Cm7, "m7", A),
  Minor6(Cm6, "m6", D),
  Dominant7(C7, "7", G),
  Minor7b5(Cm7b5, "m7b5", B),
  Diminished7(Cdim7, "dim7", G),
  MinorMajor7(Cmmaj7, "mΔ7", A),
  Major7Sharp5(Cmaj7Sharp5, "Δ7#5", C),
  Dominant7sus4(C7sus4, "7sus4", G),
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
  DiminishedTriad(CdimTriad, "dim", B),
  AugmentedTriad(CaugTriad, "aug", G),
  Sus4Triad(Csus4Triad, "sus4", G)
  ;
  
  private final Scale prototype;
  private final String typeName;
  private final Function<Note, Note> notationKey;
  
  BuiltInChordTypes(Scale prototype, String typeName, Note modeKey) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.notationKey = (n) -> n.transpose(-modeKey.ordinal());
  }

  @Override
  public Scale getPrototype() {
    return prototype;
  }

  @Override
  public String[] getModeNames() {
    return new String[0];
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  @Override
  public Function<Note, Note> notationKey() {
    return notationKey;
  }
  
  @Override
  public boolean isChord() {
    return true;
  }

}
