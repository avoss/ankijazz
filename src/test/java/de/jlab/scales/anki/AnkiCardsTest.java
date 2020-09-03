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
    deck.writeTo(Paths.get("build/anki/tritones"));
    assertThat(deck.getCsv().size()).isEqualTo(17);
    assertThat(deck.getCsv()).contains("C;Gb/F#", "F;B");
  }
  
  @Test
  public void parentScales() {
    Deck deck = anki.parentScales();
    deck.writeTo(Paths.get("build/anki/parents"));
    assertThat(deck.getCsv().size()).isEqualTo(108);
    assertThat(deck.getCsv()).contains("D Dorian;C Major Scale");
  }
  
  @Test
  public void spellParentScales() throws IOException {
    Deck deck = anki.spellParentScales();
    TestUtils.assertFileContentMatches(deck.getCsv(), AnkiCardsTest.class, "parent-scales-ordered.txt");
    deck.shuffle();
    deck.writeTo(Paths.get("build/anki/parent-scales"));
  }
  
  @Test
  public void spellAllScales() throws IOException {
    Deck deck = anki.spellAllScales();
    TestUtils.assertFileContentMatches(deck.getCsv(), AnkiCardsTest.class, "all-scales-ordered.txt");
    deck.shuffle();
    deck.writeTo(Paths.get("build/anki/all-scales"));
  }

  @Test
  public void spellTypes() {
    Deck deck = anki.spellTypes();
    deck.writeTo(Paths.get("build/anki/types"));
    assertThat(deck.getCsv()).contains(
        "Melodic Minor;1 2 b3 4 5 6 7", 
        "Lydian Dominant;1 2 3 b5 5 6 b7", 
        "Altered;1 b2 b3 3 b5 b6 b7", 
        "Harmonic Minor;1 2 b3 4 5 b6 7", 
        "Phrygian Dominant;1 b2 3 4 5 b6 b7", 
        "Mixolydian;1 2 3 4 5 6 b7");
  }
  
}
