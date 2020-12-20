package de.jlab.scales.midi;

import de.jlab.scales.theory.Note;

public class MidiUtils {
  private MidiUtils() {}
  
  public static Note midiPitchToNote(int midiPitch) {
    int ordinal = midiPitch % 12;
    return Note.values()[ordinal];
  }

  public static int noteToMidiPitch(int lowestMidiPitch, Note note) {
    int midiPitch = note.ordinal();
    while (midiPitch < lowestMidiPitch) {
      midiPitch += 12;
    }
    return midiPitch;
  }
  
}
