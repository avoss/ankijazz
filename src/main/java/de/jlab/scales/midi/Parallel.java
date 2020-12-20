package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Parallel extends CompositePart {

  public Parallel(Part part, Part... parts) {
    super(part, parts);
  }

  public Parallel(Part... parts) {
    super(parts);
  }

  @Override
  public void perform(MidiOut midiOut) {
    int clock = midiOut.getClock();
    for (Part part : parts) {
      midiOut.setClock(clock);
      part.perform(midiOut);
    }
  }

}
