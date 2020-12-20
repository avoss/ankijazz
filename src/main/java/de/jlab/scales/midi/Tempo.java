package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Tempo extends AbstractPart {
  private final int bpm;

  public Tempo(int bpm) {
    this.bpm = bpm;
  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.setTempo(bpm);
  }

}
