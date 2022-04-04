package com.ankijazz.anki;

import java.util.Map;

import com.ankijazz.difficulty.WithDifficulty;

public interface Card extends WithDifficulty, WithAssets, Comparable<Card> {
  final String CSV_DELIMITER = "\t";

  String getCsv();
  
  Map<String, Object> getJson();
  
  default int compareTo(Card that) {
    return Double.compare(this.getDifficulty(), that.getDifficulty());
  }

}
