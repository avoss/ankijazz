package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.Parts.note;

import java.util.function.BiFunction;

import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Scale;

public class PercussiveInstrument extends AbstractInstrument<PercussiveInstrument> {

  private Drum drum;

  public PercussiveInstrument(int denominator, Drum drum) {
    super(denominator);
    this.drum = drum;
  }
  
  public PercussiveInstrument bar(String pattern) {
    super.parse(pattern);
    return this;
  }

  @Override
  protected BiFunction<Event, Scale, Part> getPlayer() {
    
    return (event, scale) -> {
      return note(Drum.getMidiChannel(), drum.getMidiPitch(), event.getVelocity(), event.getNoteLength(), getDenominator());
    };
  }
}
