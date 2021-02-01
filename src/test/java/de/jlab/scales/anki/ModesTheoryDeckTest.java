package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ModesTheoryDeckTest {

  @Test
  public void testModesTheoryDeck() {
    Deck<?> deck = new ModesTheoryDeck(true);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesTheoryDeckTest.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesTheoryDeckTest.html");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "ModesTheoryDeckTest.json");
    TestUtils.writeTo(deck, 0.10);
    TestUtils.writeTo(new ModesTheoryDeck(false), 0.10);
  }

}
