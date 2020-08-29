package de.jlab.scales.lily;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class LilyScale {
  
  private ScaleInfo scaleInfo;
  private Scale scale;
  private KeySignature keySignature;

  public LilyScale(ScaleInfo scaleInfo, KeySignature keySignature) {
    this.scaleInfo = scaleInfo;
    this.keySignature = keySignature;
    this.scale = scaleInfo.getScale();
  }
  
  public String toLily() {
    return readTemplate()
      .replace("${key}", key())
      .replace("${scaleNotes}", scaleNotesWithExtendedOctave())
      .replace("${noteNames}", scaleNotesWithOctave())
      .replace("${midiChord}", drop2Chord())
      .replace("${lilyChord}", closedChord());
  }

  private CharSequence closedChord() {
    return "<" + scale.getChord(0).stream().map(this::toLilyNote).collect(joining(" ")) + ">1";
  }

  private String drop2Chord() {
    return "<" + 
        toLilyNote(scale.getNote(0)) + ", " +
        toLilyNote(scale.getNote(4)) + "' " +
        toLilyNote(scale.getNote(6)) + " " +
        toLilyNote(scale.getNote(2)) + ">1";
  }

  private String scaleNotesWithExtendedOctave() {
    return format("%s~ %s1", scaleNotesWithOctave(), lilyRoot());
  }

  private String scaleNotesWithOctave() {
    return format("%s %s", lilyNotes(), lilyRoot());
  }
  
  private String lilyNotes() {
    return scale.stream().map(this::toLilyQuarterNote).collect(joining(" "));
  }

  private String lilyRoot() {
    return toLilyNote(scale.getRoot());
  }

  private String key() {
    return toLilyNote(keySignature.getMajorKey());
  }

  private String toLilyNote(Note note) {
    return keySignature.notate(note)
        .replace("b", "f")
        .replace("#", "s")
        .toLowerCase();
  }
  
  private String toLilyQuarterNote(Note note) {
    return toLilyNote(note) + "4";
  }

  private String readTemplate() {
    try (InputStream input = LilyScale.class.getResourceAsStream("LilyScale.ly")) {
      return Utils.readString(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
