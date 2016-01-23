package de.jlab.scales.midi;

public class Parts {
  private Parts() {
  }

  public static Part seq(Part... parts) {
    return new Sequential(parts);
  }

  public static Part par(Part... parts) {
    return new Parallel(parts);
  }

  public static Part note(int channel, int pitch, int velocity, int numerator, int denominator) {
    return new NoteOn(channel, pitch, velocity, numerator, denominator);
  }

  public static Part note(int channel, int pitch, int velocity, int denominator) {
    return new NoteOn(channel, pitch, velocity, 1, denominator);
  }

  public static Part note(Drum drum, int velocity, int denominator) {
    return new NoteOn(Drum.MIDI_CHANNEL, drum.getMidiPitch(), velocity, 1, denominator);
  }

  public static Part rest(int numerator, int denominator) {
    return new Rest(numerator, denominator);
  }

  public static Part rest(int denominator) {
    return new Rest(1, denominator);
  }
  public static Part program(int midiChannel, Program program) {
    return new ProgramChange(midiChannel, program);
  }

  public static Part timeSignature(int numerator, int denominator) {
    return new TimeSignature(numerator, denominator);
  }
  
  public static Part lyrics(String text) {
    return new Lyrics(text);
  }
}
