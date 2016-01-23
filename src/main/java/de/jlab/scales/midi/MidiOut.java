package de.jlab.scales.midi;


public interface MidiOut {
  void setTimeSignature(int numerator, int denominator);
  void setTempo(int bpm);
  void addLyrics(String text);
  void advance(int numerator, int denominator);
  void noteOn(int channel, int pitch, int velocity, int numerator, int denominator);
  void programChange(int channel, int program);
  int getClock();
  void setClock(int clock);
  void save(String filename);
}
