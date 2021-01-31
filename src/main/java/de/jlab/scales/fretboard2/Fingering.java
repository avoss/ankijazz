package de.jlab.scales.fretboard2;

import java.util.List;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public interface Fingering {
  String getName();
  List<Position> getPositions();
  Scale getScale();
  Fingering transpose(Note newRoot);
}
