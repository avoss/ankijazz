package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class JamDeckTest {

  @Test
  public void test() {
    Deck<?> deck = new JamDeck("Test Jam Deck");
    TestUtils.writeTo(deck, 0.2);
  }

}
