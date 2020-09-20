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
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum Accidental {
  FLAT() {
    private final List<Note> COUNT_FLATS = asList(C, F, Bb, Eb, Ab, Db, Gb, B, E, A, D, G);

    @Override
    public Note apply(Note note) {
      return note.transpose(-1);
    }

    @Override
    public Accidental inverse() {
      return SHARP;
    }

    @Override
    public String symbol() {
      return "b";
    }

    @Override
    public Accidental twice() {
      return DOUBLE_FLAT;
    }
    
    @Override
    public int numberOfAccidentals(Note majorKey) {
      return COUNT_FLATS.indexOf(majorKey);
    }

  },
  
  SHARP {
    private final List<Note> COUNT_SHARPS = asList(C, G, D, A, E, B, Gb, Db, Ab, Eb, Bb, F);

    @Override
    public Note apply(Note note) {
      return note.transpose(1);
    }

    @Override
    public Accidental inverse() {
      return FLAT;
    }

    @Override
    public String symbol() {
      return "#";
    }

    @Override
    public Accidental twice() {
      return DOUBLE_SHARP;
    }
    @Override
    public int numberOfAccidentals(Note majorKey) {
      return COUNT_SHARPS.indexOf(majorKey);
    }
  }, 
  
  DOUBLE_FLAT {

    @Override
    public Note apply(Note note) {
      return note.transpose(-2);
    }

    @Override
    public String symbol() {
      return "bb";
    }

    @Override
    public Accidental inverse() {
      return DOUBLE_SHARP;
    }

    @Override
    public Accidental twice() {
      throw new IllegalStateException("double flat cannot be applied twice");
    }
    @Override
    public int numberOfAccidentals(Note majorKey) {
      throw new IllegalStateException("double flat cannot be applied twice");
    }
    
  },
  DOUBLE_SHARP {

    @Override
    public Note apply(Note note) {
      return note.transpose(2);
    }

    @Override
    public String symbol() {
      return "x";
    }

    @Override
    public Accidental inverse() {
      return DOUBLE_FLAT;
    }
    @Override
    public Accidental twice() {
      throw new IllegalStateException("double sharp cannot be applied twice");
    }
    @Override
    public int numberOfAccidentals(Note majorKey) {
      throw new IllegalStateException("double flat cannot be applied twice");
    }
    
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Accidental inverse();
  
  public abstract Accidental twice();
  public abstract int numberOfAccidentals(Note majorKey);

  private static final Map<Note, Accidental> majorKeyAccidentals = ImmutableMap.<Note, Accidental>builder()
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
  
  public static Accidental fromMajorKey(Note majorKey) {
    return majorKeyAccidentals.get(majorKey);
  }
 
}
