package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class PanChange extends AbstractPart {
  
  private int channel;
  private int value;

  /** value = -64 .. 63 */
  public PanChange(int channel, int value) {
    this.channel = channel;
    this.value = value;

  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.controllerChange(channel, 10, 64 + value);
  }

}
