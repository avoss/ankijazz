package de.jlab.scales.random;

public interface Sequence<T> {
  /**
   * creates the next value (often involving random algorithms). 
   * Cycles through if size() elements were created.
   */
  T next();
  
  void reset();
  
  int size();
}
