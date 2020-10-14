package de.jlab.scales.lily;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class LilyScale {
  
  private final Scale scale;
  private final KeySignature keySignature;
  private final String scaleName;
  private final boolean descending;
  private final LilyClef clef;

  public LilyScale(ScaleInfo scaleInfo) {
    this(scaleInfo, false);
  }

  public LilyScale(ScaleInfo scaleInfo, boolean descending) {
    this.clef = LilyClef.TREBLE;
    this.keySignature = scaleInfo.getKeySignature();
    this.scaleName = scaleInfo.getScaleName();
    this.scale = scaleInfo.getScale();
    this.descending = descending;
  }
  
  public String toLily() {
    return readTemplate()
      .replace("${title}", scaleName)
      .replace("${key}", key())
      .replace("${clef}", clef.getClef())
      .replace("${relativeTo}", descending ? clef.getDescendingRelativeTo() : clef.getAscendingRelativeTo())
      .replace("${scaleNotes}", scaleNotesWithExtendedOctave())
      .replace("${noteNames}", lilyNotesWithOctave())
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
    switch (scale.length()) {
    case 5:
      return format("%s %s2.", lilyNotes(), lilyRoot());
    case 6:
      return format("%s %s2", lilyNotes(), lilyRoot());
    case 7:
      return format("%s %s4 ~ %s1", lilyNotes(), lilyRoot(), lilyRoot());
    case 8:
      return format("%s %s1", lilyNotes(), lilyRoot());
    default:
      throw new IllegalStateException(format("Scales with %d notes not implemented", scale.length()));

    }
      
  }

  private String lilyNotesWithOctave() {
    return format("%s %s", lilyNotes(), lilyRoot());
  }
  
  private String lilyNotes() {
    List<Note> notes = scale.asList();
    if (descending) {
      Collections.reverse(notes);
      Collections.rotate(notes, 1);
    }
    return notes.stream().map(this::toLilyQuarterNote).collect(joining(" "));
  }

  private String lilyRoot() {
    return toLilyNote(scale.getRoot());
  }

  private String key() {
    return toLilyNote(keySignature.notationKey());
  }

  private String toLilyNote(Note note) {
    return toLilyNote(keySignature.notate(note));
  }
  
  private String toLilyNote(String notatedNote) {
    return notatedNote
        .replace("b", "f")
        .replace("#", "s")
        .replace("x", "ss")
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
