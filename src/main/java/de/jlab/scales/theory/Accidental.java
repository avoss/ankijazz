package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
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

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum Accidental {
  FLAT() {

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

  },
  SHARP {
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
    
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Accidental inverse();
  
  public abstract Accidental twice();

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

  private static final Map<Note, Integer> numberOfAccidentalsInMajorKey = ImmutableMap.<Note, Integer>builder()
      .put(C, 0)
      .put(G, 1)
      .put(D, 2)
      .put(A, 3)
      .put(E, 4)
      .put(B, 5)
      .put(Gb, 6)
      .put(Db, 5)
      .put(Ab, 4)
      .put(Eb, 3)
      .put(Bb, 2)
      .put(F, 1)
      .build();
  
  public static int numberOfAccidentalsInMajorKey(Note majorKey) {
    return numberOfAccidentalsInMajorKey.get(majorKey);
  }
  
}
