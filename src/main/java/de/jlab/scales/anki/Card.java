package de.jlab.scales.anki;

import java.nio.file.Path;

/**
 * in Anki this is a Note - but to not confuse with musical Note, its called
 * Card here.
 */
public interface Card extends Comparable<Card> {
  /** 
   * 0 = highest priority 
   */
  int getDifficulty();

  String[] getFields();
  
  default void writeAssets(Path directory) {
    // empty
  }

  default int compareTo(Card that) {
    return Integer.compare(this.getDifficulty(), that.getDifficulty());
  }

  void setId(String id);
  
}
