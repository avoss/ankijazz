package com.ankijazz.midi.song;

import com.ankijazz.theory.Scale;

public interface ChordGenerator {
  int[] midiChord(Scale chord);
}
