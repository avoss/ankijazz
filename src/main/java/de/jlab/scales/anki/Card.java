package de.jlab.scales.anki;

/**
 * in Anki this is a Note - but to not confuse with musical Note, its called
 * Card here.
 */
public interface Card extends Comparable<Card> {
  double getPriority();
  String[] getFields();
  
  @Override
  default public int compareTo(Card that) {
    return Double.compare(this.getPriority(),  that.getPriority());
  }
}
