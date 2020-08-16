package de.jlab.scales.anki;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Deck {
  private List<Card> cards = new ArrayList<>();

  public void add(String... fields) {
    add(0, fields);
  }
  
  public void add(int priority, String... fields) {
    cards.add(new BasicCard(priority, fields));
  }
  
  public void add(Card card) {
    cards.add(card);
  }

  public void writeTo(Path dir) {
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve("anki.txt");
      List<String> rows = getCsv();
      Files.write(path, rows);
      cards.forEach(c -> c.writeAssets(dir));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public List<String> getCsv() {
    List<Card> temp = new ArrayList<>(cards);
    Collections.shuffle(temp);
    Collections.sort(temp);
    return temp.stream().map(Card::getFields).map(this::toCsv).collect(toList());
  }

  private String toCsv(String... fields) {
    return Stream.of(fields).collect(joining(";"));
  }

  public List<Card> getCards() {
    return cards;
  }
}