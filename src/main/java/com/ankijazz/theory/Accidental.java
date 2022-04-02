package com.ankijazz.theory;

import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum Accidental {

  NONE(0, ""),

  FLAT(-1, "b") {
    private final List<Note> COUNT_FLATS = asList(C, F, Bb, Eb, Ab, Db, Gb, B, E, A, D, G);

    @Override
    public int numberOfAccidentals(Note majorKey) {
      return COUNT_FLATS.indexOf(majorKey);
    }

    @Override
    public boolean isEnharmonic(Note note) {
      return note == Note.C || note == Note.F;
    }

  },
  
  SHARP(1, "#") {
    private final List<Note> COUNT_SHARPS = asList(C, G, D, A, E, B, Gb, Db, Ab, Eb, Bb, F);

    @Override
    public int numberOfAccidentals(Note majorKey) {
      return COUNT_SHARPS.indexOf(majorKey);
    }

    @Override
    public boolean isEnharmonic(Note note) {
      return note == Note.E || note == Note.B;
    }

  }, 
  
  DOUBLE_FLAT(-2, "bb"),
  TRIPLE_FLAT(-3, "bbb"),
  DOUBLE_SHARP(2, "x"),
  TRIPLE_SHARP(3, "x#");

  private static final Map<Note, Accidental> preferredMajorKeyAccidentals = ImmutableMap.<Note, Accidental>builder()
      .put(C, FLAT)
      .put(G, SHARP)
      .put(D, SHARP)
      .put(A, SHARP)
      .put(E, SHARP)
      .put(B, SHARP)
      .put(Gb, FLAT)
      .put(Db, FLAT)
      .put(Ab, FLAT)
      .put(Eb, FLAT)
      .put(Bb, FLAT)
      .put(F, FLAT)
      .build();

  private final int offset;
  private final String symbol;
  
  Accidental(int offset, String symbol) {
    this.offset = offset;
    this.symbol = symbol;
  }
  
  public Accidental inverse() {
    return fromOffset(-offset);
  }

  public Accidental twice() {
    return apply(this);
  }

  public Accidental apply(Accidental that) {
    return fromOffset(this.offset + that.offset);
  }

  public int numberOfAccidentals(Note majorKey) {
    throw new UnsupportedOperationException();
  }

  public boolean isEnharmonic(Note note) {
    throw new UnsupportedOperationException();
  }

  public String symbol() {
    return symbol;
  }
  
  public int offset() {
    return offset;
  }
  
  public Note apply(Note note) {
    return note.transpose(offset);
  }
  

  public static Accidental preferredAccidentalForMajorKey(Note majorKey) {
    return preferredMajorKeyAccidentals.get(majorKey);
  }
  
  public static Accidental fromOffset(int offset) {
    return Arrays.stream(Accidental.values()).filter(a -> a.offset() == offset).findAny().get();
  }
 
}
