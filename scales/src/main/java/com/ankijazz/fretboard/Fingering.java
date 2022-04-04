package com.ankijazz.fretboard;

import java.util.List;

import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;

public interface Fingering {
  String getName();
  List<Position> getPositions();
  Scale getScale();
  Fingering transpose(Note newRoot);
}
