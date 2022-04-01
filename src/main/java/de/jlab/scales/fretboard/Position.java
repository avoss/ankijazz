package de.jlab.scales.fretboard;

import java.util.Collection;

import de.jlab.scales.theory.Scale;

public interface Position {
  /**
   * 0 = lowest string
   */
  Collection<Integer> getFrets(int stringIndex);

  int getMinFret();

  int getMaxFret();

  Tuning getTuning();
  
  Scale getScale();

  Position transpose(int semitones);
}
