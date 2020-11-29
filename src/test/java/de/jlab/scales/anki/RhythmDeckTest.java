package de.jlab.scales.anki;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.lily.LilyRhythm.Tempo;
import de.jlab.scales.lily.LilyRhythm.Type;

public class RhythmDeckTest {

  @Test
  public void test() {
    Path dir = Paths.get("build/anki");
    for (Tempo tempo: Tempo.values()) {
      for (Type type : Type.values()) {
        new RhythmDeck(tempo, type).writeTo(dir, 20);
      }
    }
  }

}
