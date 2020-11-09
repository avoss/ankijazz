package de.jlab.scales.anki;

import java.nio.file.Path;

/**
 * in Anki this is a Note - but to not confuse with musical Note, its called
 * Card here.
 */
public interface Card extends HasDifficulty, Comparable<Card> {

  String toCsv();
  String toHtml();
  
  default void writeAssets(Path directory) {
    // empty
  }

  default int compareTo(Card that) {
    return Integer.compare(this.getDifficulty(), that.getDifficulty());
  }

}
