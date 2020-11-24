package de.jlab.scales.anki;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.anki.RhythmDeck.Tempo;

public class RhythmDeckTest {

  @Test
  public void test() {
    Path dir = Paths.get("build/anki");
    new RhythmDeck(Tempo.SLOW).writeTo(dir);
    new RhythmDeck(Tempo.MEDIUM).writeTo(dir);
  }

}
