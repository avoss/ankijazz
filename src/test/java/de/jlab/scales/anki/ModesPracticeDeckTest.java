package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;

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
