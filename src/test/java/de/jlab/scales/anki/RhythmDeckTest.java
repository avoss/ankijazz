package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckTest {

  @Test
  public void generateAll() {
    TestUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 60, 81), 0);
    TestUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 100, 121), 0);
    TestUtils.writeTo((Deck<?>) new RhythmDeck(Type.PIANO, 120, 121), 0);
  }
  
}
