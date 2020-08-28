package de.jlab.scales.theory;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.CMajor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

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
  
  
  /** TODO: delete */
  public static Accidental fromScale(Scale scale) {
    int sharpFailures = tryAccidental(scale, SHARP);
    int flatFailures = tryAccidental(scale, FLAT);
    return sharpFailures == flatFailures ? SHARP : (sharpFailures < flatFailures ? SHARP : FLAT);
  }
  
  private static int tryAccidental(Scale scale, Accidental accidental) {
    if (scale.length() != CMajor.length())  { // everything relates to CMajor
      return 1000;
    }
    Note majorRoot = scale.getRoot();
    if (!CMajor.contains(majorRoot)) {
      majorRoot = accidental.inverse().apply(majorRoot);
      if (!CMajor.contains(majorRoot)) {
        return 1000;
      }
    }
    List<Note> cmajorNotes = CMajor.superimpose(majorRoot).asList();
    List<Note> scaleNotes = scale.asList();
    int failures = 0;
    for (int i = 0; i < cmajorNotes.size(); i++) {
      Note cmajorNote = cmajorNotes.get(i);
      Note scaleNote = scaleNotes.get(i);
      if (cmajorNote == scaleNote) {
        ;
      } else if (accidental.apply(cmajorNote) == scaleNote) {
        failures += 1;
      } else if (accidental.inverse().apply(cmajorNote) == scaleNote) {
        failures += 2;
      } else {
        failures += 100;
      }
    }
    return failures;
  }
  

  private static int xtryAccidental(Scale scale, Accidental accidental) {
    Set<Note> notes = scale.asSet();
    for (Note note : CMajor) {
      if (!notes.remove(note)) {
        if (!notes.remove(accidental.apply(note))) {
          notes.remove(accidental.inverse().apply(note));
        }
      }
    }
    return notes.size();
  }
  
}
