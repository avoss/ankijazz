package de.jlab.scales.midi;

public class Parts {
  private Parts() {
  }

  public static Sequential seq(Part... parts) {
    return new Sequential(parts);
  }

  public static Parallel par(Part... parts) {
    return new Parallel(parts);
  }

  public static NoteOn note(int channel, int pitch, int velocity, int numerator, int denominator) {
    return new NoteOn(channel, pitch, velocity, numerator, denominator);
  }

  public static NoteOn note(int channel, int pitch, int velocity, int denominator) {
    return new NoteOn(channel, pitch, velocity, 1, denominator);
  }

  public static NoteOn note(Drum drum, int velocity, int denominator) {
    return new NoteOn(Drum.getMidiChannel(), drum.getMidiPitch(), velocity, 1, denominator);
  }

  public static Rest rest(int numerator, int denominator) {
    return new Rest(numerator, denominator);
  }

  public static Rest rest(int denominator) {
    return new Rest(1, denominator);
  }
  
  public static ProgramChange program(int midiChannel, Program program) {
    return new ProgramChange(midiChannel, program);
  }

  public static VolumeChange volume(int midiChannel, int volume) {
    return new VolumeChange(midiChannel, volume);
  }

  public static PanChange pan(int midiChannel, int pan) {
    return new PanChange(midiChannel, pan);
  }
  
  public static TimeSignature timeSignature(int numerator, int denominator) {
    return new TimeSignature(numerator, denominator);
  }
  
  public static Tempo tempo(int bpm) {
    return new Tempo(bpm);
  }
  
}
