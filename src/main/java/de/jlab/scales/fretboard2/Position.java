package de.jlab.scales.fretboard2;

import java.util.Collection;

public interface Position {
  Collection<Integer> getFrets(int stringIndex);

  Tuning getTuning();
}
