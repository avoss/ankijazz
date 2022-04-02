package com.ankijazz.fretboard;

import java.util.List;

import com.ankijazz.theory.Note;

public interface Tuning {
  List<Note> getStrings();
  List<Integer> getMidiPitches();
}
