package de.jlab.scales.anki;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class AnkiCardsTest {
  AnkiCards anki = new AnkiCards();

  @Test
  public void testTritones() {
    Deck deck = anki.tritones();
    deck.writeTo(Paths.get("build/anki"));
    assertThat(deck.getCsv().size()).isEqualTo(17);
    assertThat(deck.getCsv()).contains("C;Gb/F#", "F;B");
  }
  
  @Test
  public void parentScales() {
    Deck deck = anki.parentScales();
    deck.writeTo(Paths.get("build/anki"));
    assertThat(deck.getCsv().size()).isEqualTo(108);
    assertThat(deck.getCsv()).contains("D Dorian;C Major Scale");
  }
  
  @Test
  public void playGuitarScales() {
    checkAndWrite(anki.playGuitarScales());
  }
  
  @Test
  public void playScales() {
    checkAndWrite(anki.playScales());
  }
  
  @Test
  public void playGuitarModes() {
    checkAndWrite(anki.playGuitarModes());
  }
  
  @Test
  public void playModes() {
    checkAndWrite(anki.playModes());
  }
  
  private void checkAndWrite(Deck deck) {
    TestUtils.assertFileContentMatches(deck.getCsv(), AnkiCardsTest.class, deck.getId() + ".txt");
    deck.shuffle(3);
    deck.writeTo(Paths.get("build/anki"));
  }

  @Test
  public void spellTypes() {
    Deck deck = anki.spellTypes();
    deck.writeTo(Paths.get("build/anki"));
    assertThat(deck.getCsv()).contains(
        "Major Scale;1 2 3 4 5 6 7",
        "Dorian;1 2 b3 4 5 6 b7",
        "Phrygian;1 b2 b3 4 5 b6 b7",
        "Lydian;1 2 3 #4 5 6 7",
        "Mixolydian;1 2 3 4 5 6 b7",
        "Aeolean;1 2 b3 4 5 b6 b7",
        "Locrian;1 b2 b3 4 b5 b6 b7",
        "Melodic Minor;1 2 b3 4 5 6 7",
        "Lydian Dominant;1 2 3 #4 5 6 b7",
        "Altered;1 b2 b3 b4 b5 b6 b7",
        "Harmonic Minor;1 2 b3 4 5 b6 7",
        "Phrygian Dominant;1 b2 3 4 5 b6 b7");
        
  }
  
}
