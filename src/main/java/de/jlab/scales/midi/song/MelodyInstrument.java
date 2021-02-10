package de.jlab.scales.midi.song;

import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Sequential;

public class MelodyInstrument implements Instrument {

  private final int ticksPerBar;

  public MelodyInstrument(int ticksPerBar) {
    this.ticksPerBar = ticksPerBar;
  }

  @Override
  public Part play(Song song) {
    Sequential seq = Parts.seq();
    for (Bar bar : song.getBars()) {
      Parallel par = Parts.par();
      if (bar.getMelody().isPresent()) {
        par.add(bar.getMelody().get());
      }
      par.add(Parts.rest(ticksPerBar, ticksPerBar));
      seq.add(par);
    }
    return seq;
  }

}
