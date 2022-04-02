package com.ankijazz.midi.song;

import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.Program;
import com.ankijazz.midi.Sequential;

public abstract class TonalInstrument<T extends AbstractInstrument<T>> extends AbstractInstrument<T> {

  private Program program;
  private int volume;
  private int pan;
  private int midiChannel;
  
  protected TonalInstrument(int beatsPerBar, int ticksPerBar, int midiChannel, Program program, int volume, int pan) {
    super(beatsPerBar, ticksPerBar);
    this.midiChannel = midiChannel;
    this.program = program;
    this.volume = volume;
    this.pan = pan;
  }

  @Override
  public Part play(Song song) {
    Sequential container = new Sequential();
    container.add(Parts.program(midiChannel, program));
    if (volume != 0) {
      container.add(Parts.volume(midiChannel, volume));
    }
    if (pan != 0) {
      container.add(Parts.pan(midiChannel, pan));
    }
    container.add(super.play(song));
    return container;
  }
  

}
