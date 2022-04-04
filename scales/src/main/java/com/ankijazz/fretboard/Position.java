package com.ankijazz.fretboard;

import java.util.Collection;

import com.ankijazz.theory.Scale;

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
