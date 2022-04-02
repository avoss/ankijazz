package com.ankijazz.midi.song;

import static com.ankijazz.midi.Parts.note;

import java.util.function.BiFunction;

import com.ankijazz.midi.Drum;
import com.ankijazz.midi.Part;
import com.ankijazz.theory.Scale;

public class PercussiveInstrument extends AbstractInstrument<PercussiveInstrument> {

  private Drum drum;

  public PercussiveInstrument(int beatsPerBar, int denominator, Drum drum) {
    super(beatsPerBar, denominator);
    this.drum = drum;
  }
  
  public PercussiveInstrument bar(String pattern) {
    super.parse(pattern);
    return this;
  }

  @Override
  protected BiFunction<Event, Scale, Part> getPlayer() {
    
    return (event, scale) -> {
      return note(Drum.getMidiChannel(), drum.getMidiPitch(), event.getVelocity(), event.getNoteLength(), getTicksPerBar());
    };
  }
}
