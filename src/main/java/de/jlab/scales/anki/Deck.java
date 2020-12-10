package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Deck extends WithAssets {

  void add(Card card);

  void writeAnki(Path dir);
  void writeJson(Path dir);
  void writeHtml(Path dir);

  /**
   * 0.0 = sort by difficulty
   * 1.0 = complete random
   */
  void sort(double randomness);

  // FIXME rename getCsv() to getAnki();
  List<String> getCsv();
  List<String> getJson();
  String getHtml();

  Deck subdeck(int numberOfCards);
  
  List<Card> getCards();

}