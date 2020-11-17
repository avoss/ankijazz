package de.jlab.scales.lily;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static de.jlab.scales.lily.Direction.DESCENDING;
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
  private final Clef clef;
  private Direction direction;
  private Note instrument;

  public LilyScale(ScaleInfo scaleInfo) {
    this(scaleInfo, ASCENDING);
  }

  public LilyScale(ScaleInfo scaleInfo, Direction direction) {
    this(scaleInfo, direction, Clef.TREBLE, Note.C);
  }
  
  public LilyScale(ScaleInfo scaleInfo, Direction direction, Clef clef, Note instrument) {
    this.direction = direction;
    this.clef = clef;
    this.instrument = instrument;
    this.keySignature = scaleInfo.getKeySignature();
    this.scaleName = scaleInfo.getScaleName();
    this.scale = scaleInfo.getScale();
  }
  

  public String toLily() {
    return readTemplate()
      .replace("${title}", scaleName)
      .replace("${key}", key())
      .replace("${clef}", clef.getClef())
      .replace("${relativeTo}", clef.getRelativeTo(direction))
      .replace("${scaleNotes}", lilyNotesWithExtendedOctave())
      .replace("${noteNames}", lilyNotesWithOctave())
      .replace("${midiChord}", drop2Chord())
      .replace("${scaleTranspose}", scaleTranspose())
      .replace("${chordsTranspose}", chordsTranspose());
  }


  private String scaleTranspose() {
    switch (instrument) {
    case C: return "";
    case Bb: return "\\transpose c bf,";
    case Eb: return "\\transpose c ef,";
    default:
      throw new IllegalStateException("Unsupported transposing instrument: " + instrument);
    }
  }

  private String chordsTranspose() {
    switch (instrument) {
    case C: return "";
    case Bb: return "\\transpose c bf,";
    case Eb: return "\\transpose c ef";
    default:
      throw new IllegalStateException("Unsupported transposing instrument: " + instrument);
    }
  }

  
  private String drop2Chord() {
    return "<" + 
        toLilyNote(scale.getNote(0)) + ", " +
        toLilyNote(scale.getNote(4)) + "' " +
        toLilyNote(scale.getNote(6)) + " " +
        toLilyNote(scale.getNote(2)) + ">1";
  }

  private String lilyNotesWithExtendedOctave() {
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
    if (direction == DESCENDING) {
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
