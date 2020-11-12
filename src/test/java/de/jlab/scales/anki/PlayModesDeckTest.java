package de.jlab.scales.anki;

import static java.util.Collections.singletonList;

import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.Clef;

public class PlayModesDeckTest {

  @Test
  public void testPlayModesDeckTreble() {
    Deck deck = new PlayModesDeck(Clef.TREBLE);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PlayModesDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(deck.getHtml()), getClass(), "PlayModesDeckTest.html.txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

  @Test
  public void testPlayModesDeckBass() {
    Deck deck = new PlayModesDeck(Clef.BASS);
//    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PlayModesDeckTest.csv.txt");
//    TestUtils.assertFileContentMatches(singletonList(deck.getHtml()), getClass(), "PlayModesDeckTest.html.txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }
  
  @Test
  public void testPlayModesGuitarDeck() {
    Deck deck = new PlayModesGuitarDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PlayModesGuitarDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(deck.getHtml()), getClass(), "PlayModesGuitarDeckTest.html.txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }
}
