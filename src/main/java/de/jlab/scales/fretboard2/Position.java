package de.jlab.scales.fretboard2;

import java.util.Collection;

import de.jlab.scales.theory.Note;

public interface Position {
  Collection<Integer> getFrets(int stringIndex);
  int getMinFret();
  int getMaxFret();

  Tuning getTuning();
  Position transpose(int semitones);
}
