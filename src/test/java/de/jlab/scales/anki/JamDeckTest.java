package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

public class JamDeckTest {

  @Test
  public void test() {
    //JamDeck deck = new JamDeck("Test Guitar", Note.C, true);
    JamDeck deck = new JamDeck("Test", Note.C, false);
    TestUtils.writeTo(deck, 0.2);
  }

}
