package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.NATURALS;

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
    return tryAccidental(s, SHARP) ? SHARP : (tryAccidental(s, FLAT) ? FLAT : SHARP);
  }

  private static boolean tryAccidental(Scale s, Accidental acc) {
    Set<Note> notes = s.getNotes();
    for (Note note : NATURALS) {
      if (!notes.remove(note)) {
        notes.remove(acc.apply(note));
      }
    }
    return notes.isEmpty();
  }
  
}
