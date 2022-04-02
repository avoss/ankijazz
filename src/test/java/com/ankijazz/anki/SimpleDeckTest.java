package com.ankijazz.anki;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ankijazz.anki.Deck;
import com.ankijazz.anki.SimpleCard;
import com.ankijazz.anki.SimpleDeck;

public class SimpleDeckTest {
  
  @Test
  public void testWriteCsv() throws IOException {
    Deck<SimpleCard> deck = new SimpleDeck<SimpleCard>("Test Deck");
    deck.add(card(1,  "A", "B", "T1"));
    deck.add(card(3,  "E", "F", "T3"));
    deck.add(card(2,  "C", "D", "T2"));
    deck.sort(0);
   
    Path dir = Paths.get("build/SimpleDeckTest");
    deck.writeAnki(dir);
    deck.writeHtml(dir);
    
    String actual = Files.readAllLines(dir.resolve("SimpleDeck.txt")).stream().collect(Collectors.joining("\n"));
    assertEquals("A\tB\tT1\nC\tD\tT2\nE\tF\tT3", actual);
  }

  private SimpleCard card(double difficulty, String front, String back, String tags) {
    SimpleCard card = new SimpleCard(difficulty, "front", "back", "tags");
    card.put("front", front);
    card.put("back", back);
    card.put("tags", tags);
    return card;
  }

}
