package de.jlab.scales.anki;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class PlayModesDeckTest {

  @Test
  public void testPlayModesDeck() {

    Deck deck = new PlayModesDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PlayModesDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(deck.getHtml()), getClass(), "PlayModesDeckTest.html.txt");
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
