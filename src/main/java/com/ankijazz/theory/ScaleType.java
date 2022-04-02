package com.ankijazz.theory;

import java.util.Set;

public interface ScaleType {
  Scale getPrototype();
  String getTypeName();
  String[] getModeNames();
  boolean isChord();
  Set<KeySignature> getKeySignatures(Note root);
}
