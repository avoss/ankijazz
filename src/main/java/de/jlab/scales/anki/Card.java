package de.jlab.scales.anki;

import java.util.Map;

import de.jlab.scales.difficulty.WithDifficulty;

public interface Card extends WithDifficulty, WithAssets, Comparable<Card> {

  // FIXME rename getCsv() to getAnki();
  String getCsv();
  
  Map<String, Object> getJson();
  
  default int compareTo(Card that) {
    return Double.compare(this.getDifficulty(), that.getDifficulty());
  }

}
