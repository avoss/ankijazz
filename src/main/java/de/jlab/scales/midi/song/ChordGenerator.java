package de.jlab.scales.midi.song;

import de.jlab.scales.theory.Scale;

public interface ChordToMidiMapper {
  int[] midiChord(Scale chord);
}
