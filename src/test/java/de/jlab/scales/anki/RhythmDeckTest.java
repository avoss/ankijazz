package de.jlab.scales.anki;

import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckTest {

  @Test
  public void testAll() {
    for (Type type : Type.values()) {
      Deck<?> deck = new RhythmDeck(type);
      TestUtils.writeTo(deck, 0.2);
    }
  }

  @Test
  @Ignore
  public void testSome() {
    Deck<?> deck = new RhythmDeck(Type.PIANO);
    TestUtils.writeTo(deck, 0);
    deck.writeHtml(Paths.get("build/anki")); // html with random ordering 
  }
  
}
