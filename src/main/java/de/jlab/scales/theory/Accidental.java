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
    public Note remove(Note note) {
      return note.transpose(1);
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
    public Note remove(Note note) {
      return note.transpose(-1);
    }

    @Override
    public String symbol() {
      return "#";
    }
  };

  public abstract Note apply(Note note);

  public abstract String symbol();

  public abstract Note remove(Note note);
  
  public static Accidental fromScale(Scale s) {
    if (s.equals(CMajor)) {
      return SHARP;
    }
    return tryAccidental(s, FLAT) ? FLAT : (tryAccidental(s, SHARP) ? SHARP : FLAT);
  }

  private static boolean tryAccidental(Scale s, Accidental acc) {
    Set<Note> notes = s.getNotes();
    for (Note note : CMajor) {
      if (!notes.remove(note)) {
        notes.remove(acc.apply(note));
      }
    }
    return notes.isEmpty();
  }
  
}
