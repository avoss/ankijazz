package de.jlab.scales.anki;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.anki.AnkiCards.Deck;

public class AnkiCardsTest {
  AnkiCards anki = new AnkiCards();

  @Test
  public void testTritones() {
    Deck deck = anki.tritones();
    deck.writeTo(Paths.get("build", "anki-tritones.txt"));
    assertThat(deck.getCards().size()).isEqualTo(17);
    assertThat(deck.getCards()).contains("C;F#/Gb", "F;B");
  }
  
  @Test
  public void testParentScales() {
    Deck deck = anki.parentScales();
    deck.writeTo(Paths.get("build", "anki-parents.txt"));
    assertThat(deck.getCards().size()).isEqualTo(108);
    assertThat(deck.getCards()).contains("D Dorian;C Major Scale");
  }
  
  @Test
  public void testSpellScales() {
    Deck deck = anki.spellScales();
    deck.writeTo(Paths.get("build", "anki-spell-scales.txt"));
    assertThat(deck.getCards()).contains("B Dorian;B C# D E F# G# A");
    assertThat(deck.getCards()).contains("Ab Melodic Minor;Ab Bb Cb Db Eb F G");
    assertThat(deck.getCards()).contains("F# Major Scale;F# G# A# B C# D# E#");
    assertThat(deck.getCards()).contains("A# Phrygian;A# B C# D# E# F# G#");
    assertThat(deck.getCards().size()).isEqualTo(144);
  }
  
  @Test
  public void testSpellTypes() {
    Deck deck = anki.spellTypes();
    deck.writeTo(Paths.get("build", "anki-spell-types.txt"));
    assertThat(deck.getCards()).contains(
        "Melodic Minor;1 2 b3 4 5 6 7", 
        "Lydian Dominant;1 2 3 b5 5 6 b7", 
        "Altered;1 b2 b3 3 b5 b6 b7", 
        "Harmonic Minor;1 2 b3 4 5 b6 7", 
        "Phrygian Dominant;1 b2 3 4 5 b6 b7", 
        "Mixolydian;1 2 3 4 5 6 b7");
  }
  
}
