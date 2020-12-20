package de.jlab.scales.midi;

import static de.jlab.scales.midi.MidiFile.ticks;
import static java.lang.String.format;

import java.nio.file.Path;

public class StringMidiOut implements MidiOut {
  
  StringBuilder sb = new StringBuilder();
  int clock = 0;

  @Override
  public void setTimeSignature(int numerator, int denominator) {
    sb.append(format("%10d TimeSignature: %d %d %d\n", clock, numerator, denominator));
  }

  @Override
  public void setTempo(int bpm) {
    sb.append(format("%10d Tempo: %d\n", clock, bpm));
  }

  @Override
  public void advance(int numerator, int denominator) {
    sb.append(format("%10d Advance by: %d/%d\n", clock, numerator, denominator));
    clock += ticks(numerator, denominator);
  }


  @Override
  public void noteOn(int channel, int pitch, int velocity, int numerator, int denominator) {
    sb.append(format("%10d Note: channel %d, pitch %d, velocity %d, %d/%d\n", clock, channel, pitch, velocity, numerator, denominator));
  }

  @Override
  public void programChange(int channel, int program) {
    sb.append(format("%10d Program: channel %d, program %d\n", clock, channel, program));
  }

  @Override
  public void controllerChange(int channel, int controller, int value) {
    sb.append(format("%10d Controller: channel %d, controller %d, value %d\n", clock, channel, controller, value));
  }
  
  @Override
  public int getClock() {
    return clock;
  }

  @Override
  public void setClock(int clock) {
    this.clock = clock;
  }

  @Override
  public void save(Path path) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public String toString() {
    return sb.toString();
  }


}
