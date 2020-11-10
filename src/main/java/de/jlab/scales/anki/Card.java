package de.jlab.scales.anki;

import java.nio.file.Path;

/**
 * in Anki this is a Note - but to not confuse with musical Note, its called
 * Card here.
 */
public interface Card extends WithDifficulty, WithAssets, Comparable<Card> {

  String toCsv();
  String toHtml();
  
  default int compareTo(Card that) {
    return Integer.compare(this.getDifficulty(), that.getDifficulty());
  }

}
