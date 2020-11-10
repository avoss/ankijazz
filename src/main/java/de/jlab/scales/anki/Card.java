package de.jlab.scales.anki;

import java.nio.file.Path;

public interface Card extends WithDifficulty, WithAssets, Comparable<Card> {

  String getCsv();
  String getHtml();
  
  default int compareTo(Card that) {
    return Integer.compare(this.getDifficulty(), that.getDifficulty());
  }

}
