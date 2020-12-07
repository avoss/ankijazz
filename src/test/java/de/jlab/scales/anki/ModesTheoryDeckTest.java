package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ModesTheoryDeckTest {

  @Test
  public void testModesTheoryDeck() {
    Deck deck = new ModesTheoryDeck();
//    FIXME remove comments
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesTheoryDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesTheoryDeckTest.html.txt");

    TestUtils.writeTo(deck, 0.10);
  }

}
