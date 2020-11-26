package de.jlab.scales.gn;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.anki.Deck;

public class GnDeckTest {

  @Test
  public void test() {
    Deck deck = new GnDeck();
    deck.shuffle(2);
    deck.writeTo(Paths.get("build/gn"));
    
    List.of(GnSong.values()).stream().filter(GnSong::hasGuitarSolo).forEach(s -> System.out.println(s));
  }

}
