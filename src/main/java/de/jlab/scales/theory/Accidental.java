package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.Set;

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
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Accidental inverse();
  
  /** works for major scales, otherwise approximate guesswork */
  public static Accidental fromScale(Scale scale) {
    if (scale.equals(CMajor)) {
      return SHARP;
    }
    int sharpFailures = tryAccidental(scale, SHARP);
    int flatFailures = tryAccidental(scale, FLAT);
    return sharpFailures == flatFailures ? FLAT : (sharpFailures < flatFailures ? SHARP : FLAT);
  }

  private static int tryAccidental(Scale scale, Accidental acc) {
    Set<Note> notes = scale.getNotes();
    for (Note note : CMajor) {
      if (!notes.remove(note)) {
        notes.remove(acc.apply(note));
      }
    }
    return notes.size();
  }
  
}
