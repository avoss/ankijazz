package com.ankijazz.theory;

import java.util.Set;

public interface ScaleType {
  Scale getPrototype();
  String getTypeName();
  String[] getModeNames();
  boolean isChord();
  
  /**
   * e.g. F#/Gb major scale has multiple KeySignatures 
   */
  Set<KeySignature> getKeySignatures(Note root);
}
