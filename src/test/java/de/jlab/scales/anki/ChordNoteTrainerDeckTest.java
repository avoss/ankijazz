package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ChordNoteTrainerDeckTest {

  @Test
  public void writeDeck() {
    TestUtils.writeTo(new ChordNoteTrainerDeck(), 0.2);
  }
  
  @Test
  public void testDeckContent() {
    TestUtils.assertFileContentMatches(new ChordNoteTrainerDeck().getJson(), getClass(), "ChordNoteTrainerDeck.json");
  }

}
