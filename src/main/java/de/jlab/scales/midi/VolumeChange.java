package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class VolumeChange extends AbstractPart {
  
  private int channel;
  private int value;

  public VolumeChange(int channel, int value) {
    this.channel = channel;
    this.value = value;

  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.controllerChange(channel, 7, value);
  }

}
