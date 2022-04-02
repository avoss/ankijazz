package com.ankijazz.anki;

import java.nio.file.Path;
import java.util.List;

public interface Deck<T extends Card> {

  void add(T card);

  void writeAnki(Path dir);
  void writeJson(Path dir);
  void writeHtml(Path dir);
  void writeAssets(Path dir);

  /**
   * 0.0 = sort by difficulty
   * 1.0 = complete random
   */
  void sort(double randomness);

  List<String> getCsv();
  List<String> getJson();
  String getHtml();

  Deck<T> subdeck(int numberOfCards);
  
  List<T> getCards();

}