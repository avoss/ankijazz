package com.ankijazz.theory;

import static com.ankijazz.theory.BuiltinScaleType.HarmonicMajor;
import static com.ankijazz.theory.BuiltinScaleType.HarmonicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Major;
import static com.ankijazz.theory.BuiltinScaleType.MelodicMinor;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Scales.C11;
import static com.ankijazz.theory.Scales.C13;
import static com.ankijazz.theory.Scales.C5;
import static com.ankijazz.theory.Scales.C6;
import static com.ankijazz.theory.Scales.C69;
import static com.ankijazz.theory.Scales.C7;
import static com.ankijazz.theory.Scales.C7flat13;
import static com.ankijazz.theory.Scales.C7flat5;
import static com.ankijazz.theory.Scales.C7flat5flat9;
import static com.ankijazz.theory.Scales.C7flat5sharp9;
import static com.ankijazz.theory.Scales.C7flat9;
import static com.ankijazz.theory.Scales.C7flat9flat13;
import static com.ankijazz.theory.Scales.C7flat9sharp11;
import static com.ankijazz.theory.Scales.C7sharp11;
import static com.ankijazz.theory.Scales.C7sharp5;
import static com.ankijazz.theory.Scales.C7sharp5flat9;
import static com.ankijazz.theory.Scales.C7sharp5sharp9;
import static com.ankijazz.theory.Scales.C7sharp9;
import static com.ankijazz.theory.Scales.C7sharp9flat13;
import static com.ankijazz.theory.Scales.C7sharp9sharp11;
import static com.ankijazz.theory.Scales.C7sus4;
import static com.ankijazz.theory.Scales.C9;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CaugTriad;
import static com.ankijazz.theory.Scales.Cdim7;
import static com.ankijazz.theory.Scales.CdimTriad;
import static com.ankijazz.theory.Scales.Cm11;
import static com.ankijazz.theory.Scales.Cm6;
import static com.ankijazz.theory.Scales.Cm69;
import static com.ankijazz.theory.Scales.Cm7;
import static com.ankijazz.theory.Scales.Cm7b5;
import static com.ankijazz.theory.Scales.Cm9;
import static com.ankijazz.theory.Scales.Cmaj7;
import static com.ankijazz.theory.Scales.Cmaj7Sharp11;
import static com.ankijazz.theory.Scales.Cmaj7Sharp5;
import static com.ankijazz.theory.Scales.Cmaj9;
import static com.ankijazz.theory.Scales.CmajTriad;
import static com.ankijazz.theory.Scales.CminTriad;
import static com.ankijazz.theory.Scales.Cmmaj7;
import static com.ankijazz.theory.Scales.Csus4Triad;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.ankijazz.theory.DegreeParser.Degree;
import com.ankijazz.theory.DegreeParser.Degrees;


public enum BuiltinChordType implements ScaleType {
  // see BuitlinChordTypeTest to figure out what to put here
  Major6(C6, "6", C, Major, "1 3 5 6"),
  Major69(C69, "69", C, Major, "1 3 5 6 9"),
  Major7(Cmaj7, "Δ7", C, Major, "1 3 5 7"),
  Major9(Cmaj9, "Δ9", C, Major, "1 3 5 7 9"),
  Major7Sharp11(Cmaj7Sharp11, "Δ7#11", G, Major, "1 3 5 7 #11"),
  Major7Sharp5(Cmaj7Sharp5, "Δ7#5", A, HarmonicMinor, "1 3 #5 7"),
  
  Minor7(Cm7, "m7", Eb, Major, "1 b3 5 b7"),
  Minor6(Cm6, "m6", Bb, Major, "1 b3 5 6"),
  Minor69(Cm69, "m69", Bb, Major, "1 b3 5 6 9"),
  Minor9(Cm9, "m9", Eb, Major, "1 b3 5 b7 9"),
  Minor11(Cm11, "m11", Eb, Major, "1 b3 5 b7 11"),
  Minor7b5(Cm7b5, "m7b5", Db, Major, "1 b3 b5 b7"),
  Diminished7(Cdim7, "dim7", E, HarmonicMinor, "1 b3 b5 bb7"),
  
  Dominant7(C7, "7", F, Major, "1 3 5 b7"),
  Dominant9(C9, "9", F, Major, "1 3 5 b7 9"),
  Dominant11(C11, "11", F, Major, "1 3 5 b7 11"),
  Dominant13(C13, "13", F, Major, "1 3 5 b7 13"),
  MinorMajor7(Cmmaj7, "mΔ7", E, HarmonicMinor, "1 b3 5 7"),
  Dominant7sus4(C7sus4, "7sus4", F, Major, "1 4 5 b7"),
  Dominant7flat9(C7flat9, "7b9", F, HarmonicMinor, "1 3 5 b7 b9"),
  Dominant7sharp9(C7sharp9, "7#9", Ab, HarmonicMajor, "1 3 5 b7 #9"),
  Dominant7flat5(C7flat5, "7b5", G, MelodicMinor, "1 3 b5 b7"), 
  Dominant7sharp5(C7sharp5, "7#5", F, HarmonicMinor, "1 3 #5 b7"), 
  Dominant7sharp11(C7sharp11, "7#11", G, MelodicMinor, "1 3 5 b7 #11"), 
  Dominant7flat13(C7flat13, "7b13", F, HarmonicMinor, "1 3 5 b7 b13"), 
  
  Dominant7flat5flat9(C7flat5flat9, "7b5b9", Db, MelodicMinor, "1 3 b5 b7 b9"),
  Dominant7sharp5flat9(C7sharp5flat9, "7#5b9", F, HarmonicMinor, "1 3 #5 b7 b9"),
  Dominant7flat5sharp9(C7flat5sharp9, "7b5#9", Db, MelodicMinor, "1 3 b5 b7 #9"),
  Dominant7sharp5sharp9(C7sharp5sharp9, "7#5#9", Db, MelodicMinor, "1 3 #5 b7 #9"),
  Dominant7flat9sharp11(C7flat9sharp11, "7b9#11", Db, MelodicMinor, "1 3 5 b7 b9 #11"), // TODO: remove, not contained in any scale 
  Dominant7flat9flat13(C7flat9flat13, "7b9b13", F, HarmonicMinor, "1 3 5 b7 b9 b13"),
  Dominant7sharp9sharp11(C7sharp9sharp11, "7#9#11", Db, MelodicMinor, "1 3 5 b7 #9 #11"), // TODO: remove, not contained in any scale
  Dominant7sharp9flat13(C7sharp9flat13, "7#9b13", Ab, HarmonicMajor, "1 3 5 b7 #9 b13"),
  
  MajorTriad(CmajTriad, "", C, Major, "1 3 5"),
  MinorTriad(CminTriad, "m", Eb, Major, "1 b3 5"),
  DiminishedTriad(CdimTriad, "dim", Db, Major, "1 b3 b5"),
  AugmentedTriad(CaugTriad, "aug", A, HarmonicMinor, "1 3 #5"),
  Sus4Triad(Csus4Triad, "sus4", C, Major, "1 4 5"),
  
  PowerChord(C5, "5", C, Major, "1 5");
  ;
  
  private final Scale prototype;
  private final String typeName;
  private final Note contextScaleRoot;
  private final ScaleType contextScaleType;
  private final Degrees degrees;
  private final boolean minor;
  private final String formula;
  
  BuiltinChordType(Scale prototype, String typeName, Note signatureScaleRoot, ScaleType signatureScaleType, String formula) {
    this.prototype = prototype;
    this.typeName = typeName;
    this.contextScaleRoot = signatureScaleRoot;
    this.contextScaleType = signatureScaleType;
    
    this.formula = formula;
    Degrees degrees = new DegreeParser().parse(formula);
    this.minor = degrees.asScale(Note.C).isMinor();
    this.degrees = minor ? degrees.relativeTo(CMajor.superimpose(-3)) : degrees;
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
    return getDegreesKeySignatures(root);
  }

  /**
   * notate chords using degree formula (e.g. 1 b3 5 b7 for minor7 chord). 
   * Major chords are notated using accidentals of major scale of chords root. 
   * Minor chords are notated using relative (natural) minor scale.
   */
  private Set<KeySignature> getDegreesKeySignatures(Note chordRoot) {
    Note majorKey = chordRoot;
    if (minor) {
      majorKey = chordRoot.transpose(3);
    }

    Set<KeySignature> result = new LinkedHashSet<>();

    for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(majorKey)) {
      Map<Note, Accidental> accidentalMap = new LinkedHashMap<>(keySignature.getAccidentalMap());
      Scale scale = degrees.getScale().transpose(chordRoot);
      for (Degree degree : degrees.getDegrees()) {
        Note scaleNote = scale.getNote(degree.getNoteIndexInScale());
        Accidental appliedAccidental = accidentalMap.get(scaleNote);
        Accidental newAccidental = Accidental.fromOffset(appliedAccidental.offset() + degree.getOffsetToApply());
        Note cmajorNote = appliedAccidental.inverse().apply(scaleNote);
        Note newScaleNote = newAccidental.apply(cmajorNote);
        accidentalMap.put(newScaleNote, newAccidental);
      }
      result.add(new KeySignature(keySignature.getNotationKey(), keySignature.getAccidental(), accidentalMap, keySignature.getNumberOfAccidentals()));
    }
    return result;
  }

  private Set<KeySignature> getContextKeySignatures(Note root) {
    return contextScaleType.getKeySignatures(contextScaleRoot.transpose(root.ordinal()));
  }

  public static Stream<BuiltinChordType> stream() {
    return Arrays.stream(BuiltinChordType.values());
  }
  
  public Note getContextScaleRoot() {
    return contextScaleRoot;
  }
  
  public ScaleType getContextScaleType() {
    return contextScaleType;
  }

  public String getFormula() {
    return formula;
  }

  public Degrees getDegrees() {
    return degrees;
  }
}
