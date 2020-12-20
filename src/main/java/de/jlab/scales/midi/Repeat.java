package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Repeat  extends AbstractPart  {

  private final Part part;
  private final int repeatCount;
  
  public Repeat(Part part, int repeatCount) {
    this.part = part;
    this.repeatCount = repeatCount;
  }

  @Override
  public void perform(MidiOut midiOut) {
    for (int i = 0; i < repeatCount; i++)
      part.perform(midiOut);
  }

}
