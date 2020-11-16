package de.jlab.scales.anki;

import static java.util.Collections.singletonList;

import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;

public class ModesPracticeDeckTest {

  @Test
  public void testPlayModesDeckTreble() {
    Deck deck = new ModesPracticeDeck(Clef.TREBLE);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesPracticeDeckTest.html.txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

  @Test
  public void testPlayModesDeckBass() {
    Deck deck = new ModesPracticeDeck(Clef.BASS);
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

  @Test
  public void testPlayModesDeckBb() {
    Deck deck = new ModesPracticeDeck(Note.Bb);
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

  @Test
  public void testPlayModesDeckEb() {
    Deck deck = new ModesPracticeDeck(Note.Eb);
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }
  
  @Test
  public void testPlayModesGuitarDeck() {
    Deck deck = new ModesPracticeGuitarDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesPracticeGuitarDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesPracticeGuitarDeckTest.html.txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }
}
