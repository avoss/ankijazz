package com.ankijazz.midi.song;

import com.ankijazz.midi.Parallel;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.Program;
import com.ankijazz.midi.Sequential;

public class MelodyInstrument implements Instrument {
  public static final int MELODY_MIDI_CHANNEL = 15;

  private final int ticksPerBar;
  private final Program program;

  private int volume;

  public MelodyInstrument(int ticksPerBar, Program program, int volume) {
    this.ticksPerBar = ticksPerBar;
    this.program = program;
    this.volume = volume;
  }

  @Override
  public Part play(Song song) {
    Sequential seq = Parts.seq();
    seq.add(Parts.program(MELODY_MIDI_CHANNEL, program));
    seq.add(Parts.volume(MELODY_MIDI_CHANNEL, volume));
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
