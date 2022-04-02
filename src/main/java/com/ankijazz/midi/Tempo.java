package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
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
