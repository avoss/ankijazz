package de.jlab.scales.midi.wav;

public class Calc {

  private final int samplingRate;
  private final long samplesPerBar;

  public Calc(int samplingRate, int beatsPerMinute) {
    this.samplingRate = samplingRate;
    this.samplesPerBar = 4L * 60L * (long) samplingRate / (long) beatsPerMinute;
  }

  public int samples(int numerator, int denominator) {
    return (int) (samplesPerBar * numerator / denominator);
  }

  public int samplingRate() {
    return samplingRate;
  }

}
