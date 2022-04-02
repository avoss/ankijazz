package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ChordNoteTrainerDeckTest {

  @Test
  public void testDeckContent() {
    TestUtils.assertFileContentMatches(new ChordNoteTrainerDeck().getJson(), getClass(), "ChordNoteTrainerDeck.json");
  }

}
