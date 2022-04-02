package de.jlab.scales.midi.song;

import de.jlab.scales.midi.Part;

public interface Instrument {
  Part play(Song song);
}
