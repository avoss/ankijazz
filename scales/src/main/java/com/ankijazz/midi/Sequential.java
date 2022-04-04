package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
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
