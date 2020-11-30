package de.jlab.scales.anki;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.lily.LilyRhythm.Tempo;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckTest {

//  @Test
//  public void testAll() {
//    Path dir = Paths.get("build/anki");
//    for (Tempo tempo: Tempo.values()) {
//      for (Type type : Type.values()) {
//        Deck deck = new RhythmDeck(tempo, type);
//        TestUtils.writeTo(deck, 0.2);
//        // TODO add assertions
//      }
//    }
//  }

  @Test
  public void testSome() {
    Path dir = Paths.get("build/anki");
    Deck deck = new RhythmDeck(Tempo.MEDIUM, Type.PIANO);
    TestUtils.writeTo(deck, 0);
  }
  
}
