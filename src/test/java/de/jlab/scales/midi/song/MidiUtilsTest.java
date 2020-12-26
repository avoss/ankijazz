package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.MidiUtils.midiPitchToNote;
import static de.jlab.scales.theory.Note.C;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.midi.MidiUtils;
import de.jlab.scales.theory.Note;

public class MidiUtilsTest {

  @Test
  public void testMidiPitchToNote() {
    assertEquals(C, midiPitchToNote(60));
    for (int octave = 0; octave < 10; octave++) {
      for (Note note : Note.values()) {
        int midiPitch = octave * 12 + note.ordinal();
        assertEquals(note, midiPitchToNote(midiPitch));
      }
    }
  }

  @Test
  public void testNoteToMidiPitch() {
    assertEquals(60, MidiUtils.noteToMidiPitch(59, Note.C));
    assertEquals(62, MidiUtils.noteToMidiPitch(59, Note.D));
    assertEquals(59, MidiUtils.noteToMidiPitch(59, Note.B));
  }
}