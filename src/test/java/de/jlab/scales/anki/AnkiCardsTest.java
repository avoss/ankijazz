package de.jlab.scales.anki;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.Test;

public class AnkiCardsTest {
  AnkiCards anki = new AnkiCards();

  @Test
  public void testTritones() {
    Deck deck = anki.tritones();
    deck.writeTo(Paths.get("build", "anki-tritones.txt"));
    assertThat(deck.getCsv().size()).isEqualTo(17);
    assertThat(deck.getCsv()).contains("C;F#/Gb", "F;B");
  }
  
  @Test
  public void parentScales() {
    Deck deck = anki.parentScales();
    deck.writeTo(Paths.get("build", "anki-parents.txt"));
    assertThat(deck.getCsv().size()).isEqualTo(108);
    assertThat(deck.getCsv()).contains("D Dorian;C Major Scale");
  }
  
  @Test
  public void spellAllScales() {
    Deck deck = anki.spellAllScales();
    deck.writeTo(Paths.get("build", "anki-spell-all-scales.txt"));
    assertThat(deck.getCsv()).contains("B Dorian;B C# D E F# G# A");
    assertThat(deck.getCsv()).contains("Ab Melodic Minor;Ab Bb Cb Db Eb F G");
    assertThat(deck.getCsv()).contains("Gb Major Scale;Gb Ab Bb Cb Db Eb F");
    assertThat(deck.getCsv()).contains("Bb Phrygian;Bb Cb Db Eb F Gb Ab");
    assertThat(deck.getCsv().size()).isEqualTo(156);
  }

  @Test
  public void spellMajorScales() {
    Deck deck = anki.spellMajorScales();
    deck.writeTo(Paths.get("build", "anki-spell-major-scales.txt"));
    assertThat(deck.getCsv()).contains("D Major Scale;D E F# G A B C#");
    assertThat(deck.getCsv()).contains("Eb Major Scale;Eb F G Ab Bb C D");
    assertThat(deck.getCsv()).contains("Gb Major Scale;Gb Ab Bb Cb Db Eb F");
    assertThat(deck.getCsv()).contains("F# Major Scale;F# G# A# B C# D# E#");
    assertThat(deck.getCsv().size()).isEqualTo(13);
  }

  @Test
  public void spellMajorTriads() {
    Deck deck = anki.spellMajorTriads();
    deck.writeTo(Paths.get("build", "anki-spell-major-triads.txt"));
    assertThat(deck.getCsv()).contains("C;C E G");
    assertThat(deck.getCsv()).contains("Eb;Eb G Bb");
  }
  
  @Test
  public void spellTypes() {
    Deck deck = anki.spellTypes();
    deck.writeTo(Paths.get("build", "anki-spell-types.txt"));
    assertThat(deck.getCsv()).contains(
        "Melodic Minor;1 2 b3 4 5 6 7", 
        "Lydian Dominant;1 2 3 b5 5 6 b7", 
        "Altered;1 b2 b3 3 b5 b6 b7", 
        "Harmonic Minor;1 2 b3 4 5 b6 7", 
        "Phrygian Dominant;1 b2 3 4 5 b6 b7", 
        "Mixolydian;1 2 3 4 5 6 b7");
  }
  
}
