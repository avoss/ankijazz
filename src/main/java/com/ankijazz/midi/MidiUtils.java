package com.ankijazz.midi;

import com.ankijazz.theory.Note;

public class MidiUtils {
  private MidiUtils() {}
  
  public static Note midiPitchToNote(int midiPitch) {
    int ordinal = midiPitch % 12;
    return Note.values()[ordinal];
  }

  public static int noteToMidiPitchBelowOrSame(int highestMidiPitch, Note note) {
    int pitch = noteToMidiPitchAboveOrSame(highestMidiPitch, note);
    if (pitch > highestMidiPitch) {
      pitch -= 12;
    }
    return pitch;
  }

  public static int noteToMidiPitchAboveOrSame(int lowestMidiPitch, Note note) {
    int midiPitch = note.ordinal();
    while (midiPitch < lowestMidiPitch) {
      midiPitch += 12;
    }
    return midiPitch;
  }

  public static Object noteToMidiPitchAbove(int lowestMidiPitch, Note note) {
    return noteToMidiPitchAboveOrSame(lowestMidiPitch, note.transpose(-1)) + 1;
  }

  public static int noteToMidiPitchBelow(int highestMidiPitch, Note note) {
    return noteToMidiPitchBelowOrSame(highestMidiPitch, note.transpose(1)) - 1;
  }
  
}
