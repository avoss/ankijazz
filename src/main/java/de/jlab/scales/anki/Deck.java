package de.jlab.scales.anki;

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
  private String id;
  private int counter;
  
  public Deck(String id) {
    this.id = "AnkiJazz-" + id;
  }
  
  public void add(String... fields) {
    add(0, fields);
  }
  
  public void add(int priority, String... fields) {
    add(new BasicCard(priority, fields));
  }
  
  public void add(Card card) {
    card.setId(String.format("%s-%04d", id, counter++));
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
  
  public void shuffle() {
    Collections.shuffle(cards);
    Collections.sort(cards);
  }
  
  public List<String> getCsv() {
    return cards.stream().map(Card::getFields).map(this::toCsv).collect(toList());
  }

  private String toCsv(String... fields) {
    return Stream.of(fields).collect(joining(";"));
  }

  public List<Card> getCards() {
    return cards;
  }
}