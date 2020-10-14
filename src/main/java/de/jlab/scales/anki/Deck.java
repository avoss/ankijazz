package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck {
  String getId();

  default void add(String... fields) {
    add(0, fields);
  }

  default void add(int difficulty, String... fields) {
    add(new BasicCard(difficulty, fields));
  }

  void add(Card card);

  void writeTo(Path dir);
  void writeHtml(MustacheTemplate template, Path dir);

  void shuffle(int randomness);

  List<String> getCsv();

  List<Card> getCards();
}