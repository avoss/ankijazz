package de.jlab.scales.gn;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.anki.Deck;

public class GnDeckTest {

  // Does not contain Siesta-Ending and Jazzmine Intro!
  @Test
  public void test() {
    Deck<?> deck = new GnDeck();
    TestUtils.writeTo(deck, 1);
    List.of(GnSong.values()).stream().filter(GnSong::hasGuitarSolo).forEach(s -> System.out.println(s));
  }

}
