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
  int getPriority();

  String[] getFields();
  void writeAssets(Path directory);

  @Override
  default public int compareTo(Card that) {
    return Integer.compare(this.getPriority(), that.getPriority());
  }
}
