package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

public class GuitarDeckTest {

  @Test
  public void test() {
    Deck deck = new GuitarDeck(new SimpleDeck("ID"));
    deck.add("A;B");
    assertEquals("[A;B;Low;<img src=\"AnkiJazz-FretboardLow.png\">, A;B;Medium;<img src=\"AnkiJazz-FretboardMedium.png\">, A;B;High;<img src=\"AnkiJazz-FretboardHigh.png\">]", deck.getCsv().toString());
  }

}
