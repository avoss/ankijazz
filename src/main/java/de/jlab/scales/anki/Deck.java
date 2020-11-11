package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck {

  default void add(int difficulty, String front, String back, String tags) {
    add(new SimpleCard(difficulty, front, back, tags));
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