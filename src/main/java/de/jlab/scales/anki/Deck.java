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

  void add(String... fields) {
    add(0, fields);
  }
  void add(int priority, String... fields) {
    cards.add(new BasicCard(priority, fields));
  }

  public void writeTo(Path path) {
    List<String> rows = getCsv();
    try {
      Files.write(path, rows);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public List<String> getCsv() {
    List<Card> temp = new ArrayList<>(cards);
    Collections.shuffle(temp);
    Collections.sort(temp);
    List<String> rows = temp.stream().map(Card::getFields).map(this::toCsv).collect(toList());
    return rows;
  }

  private String toCsv(String... fields) {
    return Stream.of(fields).collect(joining(";"));
  }

  public List<Card> getCards() {
    return cards;
  }
}