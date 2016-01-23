package de.jlab.scales.midi;

import static de.jlab.scales.midi.Drum.Cowbell;
import static de.jlab.scales.midi.Drum.MetronomeBell;
import static de.jlab.scales.midi.Drum.MetronomeClick;
import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

public class Base16 {
  Part r = rest(16);
  Part cb = note(Cowbell, 127, 24);
  Part hi = seq(note(MetronomeBell, 127, 24), r);
  Part lo = seq(note(MetronomeClick, 127, 24), r);
  Part HI = seq(cb, r);

  Part q = seq(lo, r, r, r);
  Part Q = seq(hi, r, r, r);
  Part bar = seq(Q, q, q, q);

}
