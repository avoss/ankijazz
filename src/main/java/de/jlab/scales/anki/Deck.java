package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck {

  void add(String... fields);

  void add(int priority, String... fields);

  void add(Card card);

  void writeTo(Path dir);

  void shuffle();

  List<String> getCsv();

  List<Card> getCards();

}