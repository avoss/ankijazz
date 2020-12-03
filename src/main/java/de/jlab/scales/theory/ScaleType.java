package de.jlab.scales.theory;

import java.util.function.Function;

public interface ScaleType {
  Scale getPrototype();
  String getTypeName();
  String[] getModeNames();
  Function<Note, Note> notationKey();
  boolean isChord();
}
