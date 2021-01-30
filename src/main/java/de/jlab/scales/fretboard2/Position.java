package de.jlab.scales.fretboard2;

import java.util.Collection;

public interface Position {
  /**
   * 0 = lowest string
   */
  Collection<Integer> getFrets(int stringIndex);

  int getMinFret();

  int getMaxFret();

  Tuning getTuning();

  Position transpose(int semitones);
}
