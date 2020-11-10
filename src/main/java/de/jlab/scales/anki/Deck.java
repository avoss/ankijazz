package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck {

  default void add(String... fields) {
    add(0, fields);
  }

  default void add(int difficulty, String... fields) {
    add(new SimpleCard(difficulty, fields));
  }

  void add(Card card);

  void writeTo(Path dir);
  void writeHtml(Path dir);

  void shuffle(int randomness);

  // TODO rename to "toCsv()" and "toHtml()" to match Card interface
  List<String> getCsv();
  String getHtml();

  List<Card> getCards();
}