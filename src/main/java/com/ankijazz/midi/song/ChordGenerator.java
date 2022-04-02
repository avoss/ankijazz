package de.jlab.scales.midi.song;

import de.jlab.scales.theory.Scale;

public interface ChordGenerator {
  int[] midiChord(Scale chord);
}
