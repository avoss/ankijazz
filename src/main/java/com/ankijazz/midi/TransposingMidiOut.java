package de.jlab.scales.midi;

import java.nio.file.Path;

import de.jlab.scales.theory.Note;

public class TransposingMidiOut implements MidiOut {
  private final MidiOut delegate;
  private final int semitones;

  public TransposingMidiOut(MidiOut delegate, Note instrument) {
    this.delegate = delegate;
    switch (instrument) {
    case C:
      semitones = 0;
      break;
    case Bb:
      semitones = -2;
      break;
    case Eb:
      semitones = 3;
      break;
    default:
      throw new IllegalArgumentException("invalid instrument " + instrument);
    }

  }

  public void setTimeSignature(int numerator, int denominator) {
    delegate.setTimeSignature(numerator, denominator);
  }

  public void setTempo(int bpm) {
    delegate.setTempo(bpm);
  }

  public void advance(int numerator, int denominator) {
    delegate.advance(numerator, denominator);
  }

  public void noteOn(int channel, int pitch, int velocity, int numerator, int denominator) {
    if (channel != Drum.getMidiChannel()) {
      pitch += semitones;
    }
    delegate.noteOn(channel, pitch, velocity, numerator, denominator);
  }

  public void programChange(int channel, int program) {
    delegate.programChange(channel, program);
  }

  public void controllerChange(int channel, int controller, int value) {
    delegate.controllerChange(channel, controller, value);
  }

  public int getClock() {
    return delegate.getClock();
  }

  public void setClock(int clock) {
    delegate.setClock(clock);
  }

  public void save(Path file) {
    delegate.save(file);
  }

}
