package de.jlab.scales.anki;

import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ModesTheoryDeckTest {

  @Test
  public void testModesTheoryDeck() {
    Deck deck = new ModesTheoryDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "ModesTheoryDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(deck.getHtml(), getClass(), "ModesTheoryDeckTest.html.txt");
    
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

}
