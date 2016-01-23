package de.jlab.scales.midi;

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
