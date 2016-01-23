package de.jlab.scales.algo;


import de.jlab.scales.random.EmpiricalDistribution;
import de.jlab.scales.random.Limiter;
import de.jlab.scales.random.PatternBoolean;
import de.jlab.scales.random.RangeConverter;
import de.jlab.scales.random.Sequence;
import de.jlab.scales.random.UniformRange;

public class InstrumentBuilder {

  private int midiChannel;
  private Sequence<int[]> pitches;
  private Sequence<Boolean> rhythm;
  private Sequence<Integer> velocity;
  private Sequence<Integer>  length;

  public InstrumentBuilder(int midiChannel, Sequence<int[]> pitches) {
    this.midiChannel = midiChannel;
    this.pitches = pitches;
  }
  
  public InstrumentBuilder rhythm(String pattern) {
    rhythm = new PatternBoolean(pattern);
    return this;
  }
  public InstrumentBuilder rhythm(String pattern, String limit) {
    rhythm = new Limiter(limit, new PatternBoolean(pattern));
    return this;
  }
  
  public InstrumentBuilder velocity(int min, int max) {
    velocity = new UniformRange(min, max);
    return this;
  }
  
  public InstrumentBuilder length(String pattern) {
    EmpiricalDistribution distribution = new EmpiricalDistribution(1, pattern);
    length = new RangeConverter(distribution, 1, distribution.size());
    return this;
  }
  
  public Instrument build() {
    DefaultInstrument instrument = new DefaultInstrument();
    instrument.setDenominator(16); // TODO
    instrument.setMidiChannel(midiChannel);
    instrument.setPitchGenerator(pitches);
    instrument.setRhythmGenerator(rhythm);
    instrument.setVelocityGenerator(velocity);
    instrument.setLengthGenerator(length);
    return instrument;
  }

}
