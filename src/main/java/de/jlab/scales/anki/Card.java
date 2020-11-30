package de.jlab.scales.anki;

import de.jlab.scales.difficulty.WithDifficulty;

public interface Card extends WithDifficulty, WithAssets, Comparable<Card> {

  String getCsv();
  String getHtml();
  
  default int compareTo(Card that) {
    return Double.compare(this.getDifficulty(), that.getDifficulty());
  }

}
