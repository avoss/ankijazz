package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.anki.Deck;
import com.ankijazz.anki.ModesTheoryDeck;

public class ModesTheoryDeckTest {

  @Test
  public void testModesTheoryDeck() {
    Deck<?> deck = new ModesTheoryDeck(true);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesTheoryDeckTest.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesTheoryDeckTest.html");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "ModesTheoryDeckTest.json");
  }

}
