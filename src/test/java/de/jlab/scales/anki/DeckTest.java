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
    Deck deck = new SimpleDeck("Test Deck");
    deck.add(1,  "A", "B", "T1");
    deck.add(3,  "E", "F", "T3");
    deck.add(2,  "C", "D", "T2");
    deck.shuffle(0);
    Path dir = Paths.get("build");
    deck.writeTo(dir);
    
    String actual = Files.readAllLines(dir.resolve("SimpleDeck.txt")).stream().collect(Collectors.joining("\n"));
    assertEquals("A;B;T1\nC;D;T2\nE;F;T3", actual);
  }

}
