package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
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
