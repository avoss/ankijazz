package de.jlab.scales.random;

public interface Context {
  
  /**
   * allows probabilities to change depending on the beat we are on
   */
  int getPosition();
}
