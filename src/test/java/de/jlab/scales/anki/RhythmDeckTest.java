package de.jlab.scales.anki;

import java.nio.file.Paths;

import org.junit.Test;

public class RhythmDeckTest {

  @Test
  public void test() {
    Deck deck = new RhythmDeck();
    //deck.shuffle(30);
    deck.writeTo(Paths.get("build/anki"));
  }

}
