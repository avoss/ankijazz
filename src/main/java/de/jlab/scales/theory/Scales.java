package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;

import java.util.Collection;

public class Scales {

  public static final Scale CMajor = new Scale(C, D, E, F, G, A, B);
  public static final Scale CMelodicMinor = new Scale(C, D, Eb, F, G, A, B);
  public static final Scale CHarmonicMinor = new Scale(C, D, Eb, F, G, Ab, B);
  public static final Scale CHarmonicMajor = new Scale(C, D, E, F, G, Ab, B);
  public static final Scale CDiminishedHalfWhole = new Scale(C, Db, Eb, E, Gb, G, A, Bb);
  public static final Scale CWholeTone = new Scale(C, D, E, Gb, Ab, Bb);

  public static final Scale CMinorPentatonic = new Scale(C, Eb, F, G, Bb);
  public static final Scale CMinor6Pentatonic = new Scale(C, Eb, F, G, A);

  public static final Scale Cmaj7 = new Scale(C, E, G, B);
  public static final Scale Cm7 = new Scale(C, Eb, G, Bb);
  public static final Scale Cm6 = new Scale(C, Eb, G, A);
  public static final Scale C7 = new Scale(C, E, G, Bb);
  public static final Scale Cm7b5 = new Scale(C, Eb, Gb, Bb);
  public static final Scale Cdim7 = new Scale(C, Eb, Gb, A);
  public static final Scale Cmmaj7 = new Scale(C, Eb, G, B);
  public static final Scale Cmaj7Sharp5 = new Scale(C, E, Ab, B);
  public static final Scale Cmaj7Sharp11 = new Scale(C, E, Gb, B);
  public static final Scale C7sus4 = new Scale(C, F, G, Bb);

  public static final Scale C7flat9 = new Scale(C, E, G, Bb, Db);
  public static final Scale C7sharp9 = new Scale(C, E, G, Bb, Eb);
  public static final Scale C7flat5 = new Scale(C, E, Gb, Bb);
  public static final Scale C7sharp5 = new Scale(C, E, Ab, Bb);
  public static final Scale C7flat5flat9 = new Scale(C, E, Gb, Bb, Db);
  public static final Scale C7sharp5flat9 = new Scale(C, E, Ab, Bb, Db);
  public static final Scale C7flat5sharp9 = new Scale(C, E, Gb, Bb, Eb);
  public static final Scale C7sharp5sharp9 = new Scale(C, E, Ab, Bb, Eb);

  public static final Scale CmajTriad = new Scale(C, E, G);
  public static final Scale CminTriad = new Scale(C, Eb, G);
  public static final Scale CdimTriad = new Scale(C, Eb, Gb);
  public static final Scale CaugTriad = new Scale(C, E, Ab);
  public static final Scale CsusTriad = new Scale(C, F, G);

  public static Scale parseChord(String name) {
    return ChordParser.parseChord(name);
  }
 
}
