package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Sequential extends CompositePart {

  public Sequential(Part part, Part... parts) {
    super(part, parts);
  }

  public Sequential(Part... parts) {
    super(parts);
  }

  @Override
  public void perform(MidiOut midiOut) {
    for (Part part : parts)
      part.perform(midiOut);
  }

}
