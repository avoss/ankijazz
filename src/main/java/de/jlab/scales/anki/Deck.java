package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck extends WithAssets {

  default void add(double difficulty, String front, String back, String tags) {
    add(new SimpleCard(difficulty, front, back, tags));
  }

  void add(Card card);

  void writeHtml(Path dir);


  /**
   * 0.0 = sort by difficulty
   * 1.0 = complete random
   */
  void sort(double randomness);

  List<String> getCsv();
  String getHtml();

  List<Card> getCards();
}