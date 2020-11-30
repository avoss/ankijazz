package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;

public class ModesPracticeDeckTest {

  private static final double RND = 0.2;

  @Test
  public void testPlayModesDeckTreble() {
    Deck deck = new ModesPracticeDeck(Note.C);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesPracticeDeckTest.html.txt");
    TestUtils.writeTo(deck, RND);
  }

  @Test
  public void testPlayModesDeckBass() {
    Deck deck = new ModesPracticeDeck(Clef.BASS);
    TestUtils.writeTo(deck, RND);
  }

  @Test
  public void testPlayModesDeckBb() {
    Deck deck = new ModesPracticeDeck(Note.Bb);
    TestUtils.writeTo(deck, RND);
  }

  @Test
  public void testPlayModesDeckEb() {
    Deck deck = new ModesPracticeDeck(Note.Eb);
    TestUtils.writeTo(deck, RND);
  }
  
  @Test
  public void testPlayModesGuitarDeck() {
    Deck deck = new ModesPracticeGuitarDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeGuitarDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesPracticeGuitarDeckTest.html.txt");
    TestUtils.writeTo(deck, RND);
  }
}
