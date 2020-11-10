package de.jlab.scales.anki;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class PlayModesDeckTest {

  @Test
  public void test() {

    Deck deck = new PlayModesDeck();
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PlayModesDeckTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(deck.getHtml()), getClass(), "PlayModesDeckTest.html.txt");
    deck.writeTo(Paths.get("build/anki"));
  }

}
