package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck {

  default void add(double difficulty, String front, String back, String tags) {
    add(new SimpleCard(difficulty, front, back, tags));
  }

  void add(Card card);

  void writeTo(Path dir);
  void writeHtml(Path dir);


  /**
   * 0.0 = no change, just sort by difficulty
   * 1.0 = complete random
   */
  void sort(double randomness);

  List<String> getCsv();
  String getHtml();

  List<Card> getCards();
}