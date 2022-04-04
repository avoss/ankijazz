package com.ankijazz.theory;

import java.util.Set;

/**
 * Describes a scale (e.g. Major Scale) or a chord (e.g. Minor7 Chord).
 */

public interface ScaleType {
  
  /**
   * returns a Scale of that type with root = Note.C
   */
  Scale getPrototype();

  /**
   * e.g. "Major Scale" or "m7" for minor 7 chords
   */
  String getTypeName();
  
  /**
   * Mode names of scales. 
   * Empty for chords, the modes of chords (aka inversions) can be computed in ScaleUniverse if desired. 
   */
  String[] getModeNames();
  
  /**
   * ugly, I know.
   */
  boolean isChord();
  
  /**
   * e.g. F#/Gb major scale has multiple KeySignatures 
   */
  Set<KeySignature> getKeySignatures(Note root);
}
