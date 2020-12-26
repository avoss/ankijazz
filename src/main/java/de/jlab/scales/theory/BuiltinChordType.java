package de.jlab.scales.theory;

import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat5;
import static de.jlab.scales.theory.Scales.*;
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
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp11;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp5;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.CminTriad;
import static de.jlab.scales.theory.Scales.Cmmaj7;
import static de.jlab.scales.theory.Scales.Csus4Triad;
import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Set;


public enum BuiltinChordType implements ScaleType {
  // see BuitlinChordTypeTest to figure out what to put here
  Major7(Cmaj7, "Δ7", C, Major),
  Major7Sharp11(Cmaj7Sharp11, "Δ7#11", G, Major),
  Minor7(Cm7, "m7", Eb, Major),
  Minor6(Cm6, "m6", Bb, Major),
  Minor9(Cm9, "m9", Bb, Major),
  Minor11(Cm11, "m11", Bb, Major),
  Dominant7(C7, "7", F, Major),
  Dominant9(C9, "9", F, Major),
  Dominant13(C13, "13", F, Major),
  Minor7b5(Cm7b5, "m7b5", Db, Major),
  Diminished7(Cdim7, "dim7", E, HarmonicMinor),
  MinorMajor7(Cmmaj7, "mΔ7", C, MelodicMinor),
  Major7Sharp5(Cmaj7Sharp5, "Δ7#5", A, MelodicMinor),
  Dominant7sus4(C7sus4, "7sus4", F, Major),
  Dominant7flat9(C7flat9, "7b9", F, HarmonicMinor),
  Dominant7sharp9(C7sharp9, "7#9", A, HarmonicMajor),
  Dominant7flat5(C7flat5, "7b5", G, MelodicMinor), 
  Dominant7sharp5(C7sharp5, "7#5", F, MelodicMinor), 
  Dominant7flat5flat9(C7flat5flat9, "7b5b9", Db, MelodicMinor),
  Dominant7sharp5flat9(C7sharp5flat9, "7#5b9", Db, MelodicMinor),
  Dominant7flat5sharp9(C7flat5sharp9, "7b5#9", Db, MelodicMinor),
  Dominant7sharp5sharp9(C7sharp5sharp9, "7#5#9", Db, MelodicMinor),
  
  MajorTriad(CmajTriad, "", C, Major),
  MinorTriad(CminTriad, "m", Eb, Major),
  DiminishedTriad(CdimTriad, "dim", Db, Major),
  AugmentedTriad(CaugTriad, "aug", A, MelodicMinor),
  Sus4Triad(Csus4Triad, "sus4", C, Major)
  ;
  
  private final Scale prototype;
  private final String typeName;
  private final Note containingScaleRoot;
  private final ScaleType containingScaleType;
  
  BuiltinChordType(Scale prototype, String typeName, Note containingScaleRoot, ScaleType containingScaleType) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.containingScaleRoot = containingScaleRoot;
    this.containingScaleType = containingScaleType;
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
  public boolean isChord() {
    return true;
  }

  @Override
  public Set<KeySignature> getKeySignatures(Note root) {
    Scale chord = prototype.transpose(root);
    // TODO should use all containing scale types, not just one
    Set<Accidental> accidentals = containingScaleType.getKeySignatures(containingScaleRoot.transpose(root)).stream().map(KeySignature::getAccidental).collect(toCollection(LinkedHashSet::new));
    return accidentals.stream().map(accidental -> KeySignature.fromChord(chord, accidental)).collect(toCollection(LinkedHashSet::new));
  }

}
