package de.jlab.scales.midi;

public enum Microtime {
  ;//SIXTEENTH(MidiFile.PPQ / 4);
  
  private final int midiTicks;

  Microtime(int midiTicks) {
    this.midiTicks = midiTicks;
  }
  public int getMidiTicks() {
    return midiTicks;
  }

}
