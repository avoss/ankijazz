package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ChordNoteTrainerDeckProd {

  @Test
  public void writeDeck() {
    TestUtils.writeTo(new ChordNoteTrainerDeck(), 0.2);
  }
  
}
