package de.jlab.scales.algo;

import de.jlab.scales.midi.Part;

public interface Instrument {
  public Part play(int songLengthInMicrotimeUnits);
}
