package com.ankijazz.midi.song;

import static com.ankijazz.midi.MidiUtils.midiPitchToNote;
import static com.ankijazz.theory.Note.C;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ankijazz.midi.MidiUtils;
import com.ankijazz.theory.Note;

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
  public void testNoteToMidiPitchAboveOrSame() {
    assertEquals(60, MidiUtils.noteToMidiPitchAboveOrSame(59, Note.C));
    assertEquals(62, MidiUtils.noteToMidiPitchAboveOrSame(59, Note.D));
    assertEquals(59, MidiUtils.noteToMidiPitchAboveOrSame(59, Note.B));
  }
  @Test
  public void testNoteToMidiPitchBelowOrSame() {
    assertEquals(60, MidiUtils.noteToMidiPitchBelowOrSame(60, Note.C));
    assertEquals(50, MidiUtils.noteToMidiPitchBelowOrSame(60, Note.D));
    assertEquals(59, MidiUtils.noteToMidiPitchBelowOrSame(60, Note.B));
  }

  @Test
  public void testNoteToMidiPitchAbove() {
    assertEquals(60, MidiUtils.noteToMidiPitchAbove(59, Note.C));
    assertEquals(62, MidiUtils.noteToMidiPitchAbove(59, Note.D));
    assertEquals(59+12, MidiUtils.noteToMidiPitchAbove(59, Note.B));
  }
  @Test
  public void testNoteToMidiPitchBelow() {
    assertEquals(60-12, MidiUtils.noteToMidiPitchBelow(60, Note.C));
    assertEquals(50, MidiUtils.noteToMidiPitchBelow(60, Note.D));
    assertEquals(59, MidiUtils.noteToMidiPitchBelow(60, Note.B));
  }
  
  @Test
  public void testSong() {
    assertEquals("| Cm7 | F7 |", MidiTestUtils.song("Cm7 F7").toString());
  }
}
