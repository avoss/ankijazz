package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

public class WAV {

  protected Part r = rest(16);
  protected Part hi = seq(note(9, 104, 127, 24), r);
  protected Part lo = seq(note(9, 98, 127, 24), r);

}
