package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.anki.Deck;
import com.ankijazz.anki.ModesPracticeDeck;
import com.ankijazz.anki.ModesPracticeGuitarDeck;
import com.ankijazz.theory.Note;

public class ModesPracticeDeckTest {

  @Test
  public void testPlayModesDeckC() {
    Deck<?> deck = new ModesPracticeDeck(Note.C);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeDeckTest.txt");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "ModesPracticeDeckTest.json");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesPracticeDeckTest.html");
  }

  
  @Test
  public void testPlayModesGuitarDeck() {
    Deck<?> deck = new ModesPracticeGuitarDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeGuitarDeckTest.txt");
  }
}
