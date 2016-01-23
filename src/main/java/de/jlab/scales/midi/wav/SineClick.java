package de.jlab.scales.midi.wav;

public class SineClick {
  
  private int samplingRate;

  public SineClick(int samplingRate) {
    this.samplingRate = samplingRate;
  }

  public short[] noteOn(int midiPitch, int velocity) {
    int size = 500;
    short[] samples = new short[size];
    double freq = midi2Hz(midiPitch);
    double incr = freq / samplingRate * 2.0 * Math.PI;
    double angle = 0.0;
    for (int i = 0; i < size; i++) {
      double value = Short.MAX_VALUE * Math.sin(angle) * velocity / 127.0;
      samples[i] = (short)value;
      angle += incr;
    }
    return samples;
  }

  static double midi2Hz(int midiPitch) {
    // http://subsynth.sourceforge.net/midinote2freq.html
    final double A = 440;
    return (A / 32.0) * (Math.pow(2.0, ((midiPitch - 9) / 12.0)));
  }

}
