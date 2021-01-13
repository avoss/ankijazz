package de.jlab.scales.anki;

import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckTest {

  @Test
  @Ignore
  public void testAll() {
    for (Type type : Type.values()) {
      Deck<?> deck = new RhythmDeck(type);
      TestUtils.writeTo(deck, 0.0);
    }
  }

  @Test
  public void testSome() {
    Deck<?> deck = new RhythmDeck(Type.PIANO);
    TestUtils.writeTo(deck, 0);
  }
  
}
