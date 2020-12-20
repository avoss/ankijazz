package de.jlab.scales.theory;

import java.util.Set;

public interface ScaleType {
  Scale getPrototype();
  String getTypeName();
  String[] getModeNames();
  boolean isChord();
  Set<? extends KeySignature> getKeySignatures(Note root);
}
