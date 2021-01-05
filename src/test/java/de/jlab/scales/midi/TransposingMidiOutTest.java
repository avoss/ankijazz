package de.jlab.scales.midi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.theory.Note;

public class TransposingMidiOutTest {

  @Test
  public void test() {
    MockMidiOut mock = new MockMidiOut();
    TransposingMidiOut out = new TransposingMidiOut(mock, Note.Bb);
    Parts.note(Drum.AcousticBassDrum, 100, 16).perform(out);
    Parts.note(1, 60, 100, 16).perform(out);
    assertEquals(mock.getNotes().get(0).getPitch(), Drum.AcousticBassDrum.getMidiPitch());
    assertEquals(mock.getNotes().get(1).getPitch(), 58);
  }

}
