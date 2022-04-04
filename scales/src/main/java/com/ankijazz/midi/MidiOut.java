package com.ankijazz.midi;

import java.nio.file.Path;

public interface MidiOut {
  void setTimeSignature(int numerator, int denominator);
  void setTempo(int bpm);
  void advance(int numerator, int denominator);
  void noteOn(int channel, int pitch, int velocity, int numerator, int denominator);
  void programChange(int channel, int program);
  void controllerChange(int channel, int controller, int value);
  int getClock();
  void setClock(int clock);
  void save(Path file);
}
