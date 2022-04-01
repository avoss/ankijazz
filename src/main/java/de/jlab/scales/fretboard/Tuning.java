package de.jlab.scales.fretboard2;

import java.util.List;

import de.jlab.scales.theory.Note;

public interface Tuning {
  List<Note> getStrings();
  List<Integer> getMidiPitches();
}
