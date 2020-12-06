package de.jlab.scales.theory;

public interface ScaleType {
  Scale getPrototype();
  String getTypeName();
  String[] getModeNames();
  boolean isChord();
  KeySignature getKeySignature(Note root);
}
