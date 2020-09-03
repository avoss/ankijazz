package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.Test;

public class DeckTest {

  @Test
  public void testWriteCsv() throws IOException {
    Deck deck = new Deck("xx");
    deck.add(1,  "A", "B");
    deck.add(3,  "E", "F");
    deck.add(2,  "C", "D");
    deck.shuffle();
    Path path = Paths.get("build/csvtest");
    deck.writeTo(path);
    
    String actual = Files.readAllLines(path.resolve("anki.txt")).stream().collect(Collectors.joining("\n"));
    assertEquals("A;B\nC;D\nE;F", actual);
  }

}
