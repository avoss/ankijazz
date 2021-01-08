package de.jlab.scales.anki;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class JamDeckTest {

  @Test
  @Ignore
  public void testAll() {
    TestUtils.writeTo(new JamDeck("Practice Chords (C-Instrument)", Note.C, false), 0.2);
    TestUtils.writeTo(new JamDeck("Practice Chords (Guitar)", Note.C, true), 0.2);
    TestUtils.writeTo(new JamDeck("Practice Chords (Bb-Instrument)", Note.Bb, false), 0.2);
    TestUtils.writeTo(new JamDeck("Practice Chords (Eb-Instrument)", Note.Eb, false), 0.2);
  }

  @Test
  public void testC() {
    TestUtils.writeTo(new JamDeck("Practice Chords (C-Instrument)", Note.C, false), 0.2);
  }
}
