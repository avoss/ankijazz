package de.jlab.scales.lily;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.*;

public class LilyScale {
  
  private ScaleInfo scaleInfo;
  private Scale scale;
  private Accidental accidental;

  public LilyScale(ScaleInfo scaleInfo, Accidental accidental) {
    this.scaleInfo = scaleInfo;
    this.accidental = accidental;
    this.scale = scaleInfo.getScale();
  }
  
  public String toLily() {
    return readTemplate()
      .replace("${key}", key())
      .replace("${scaleNotes}", scaleNotesWithExtendedOctave())
      .replace("${chordNotes}", scaleNotesWithOctave());
  }



  private String scaleNotesWithExtendedOctave() {
    return format("%s~ %s1", scaleNotesWithOctave(), lilyRoot());
  }

  private CharSequence scaleNotesWithOctave() {
    return format("%s %s", lilyNotes(), lilyRoot());
  }
  
  private String lilyNotes() {
    return scale.stream().map(this::toLilyQuarterNote).collect(joining(" "));
  }

  private String lilyRoot() {
    return toLilyNote(scale.getRoot());
  }

  private String key() {
    return toLilyNote(scaleInfo.getParent().getRoot());
  }

  private String toLilyNote(Note note) {
    return scale.noteName(note, accidental)
        .replace("b", "f")
        .replace("#", "s")
        .toLowerCase();
  }
  
  private String toLilyQuarterNote(Note note) {
    return toLilyNote(note) + "4";
  }

  private String readTemplate() {
    try (InputStream input = LilyScale.class.getResourceAsStream("LilyScale.ly")) {
      return Utils.read(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
