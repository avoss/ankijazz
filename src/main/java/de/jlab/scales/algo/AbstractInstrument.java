package de.jlab.scales.algo;

import de.jlab.scales.midi.Program;
import de.jlab.scales.random.Sequence;

public abstract class AbstractInstrument implements Instrument {
  
  protected int denominator;
  
  protected int midiChannel;
  protected Program program;
  
  protected Sequence<int[]> pitchGenerator; 
  protected Sequence<Boolean> rhythmGenerator;
  protected Sequence<Integer> lengthGenerator;
  protected Sequence<Integer> velocityGenerator;
  

  protected void reset() {
    pitchGenerator.reset();
    rhythmGenerator.reset();
    lengthGenerator.reset();
    velocityGenerator.reset();
  }

  public int getDenominator() {
    return denominator;
  }

  public void setDenominator(int denominator) {
    this.denominator = denominator;
  }

  public Sequence<int[]> getPitchGenerator() {
    return pitchGenerator;
  }

  public void setPitchGenerator(Sequence<int[]> pitchGenerator) {
    this.pitchGenerator = pitchGenerator;
  }

  public int getMidiChannel() {
    return midiChannel;
  }

  public void setMidiChannel(int midiChannel) {
    this.midiChannel = midiChannel;
  }

  public Program getProgram() {
    return program;
  }

  public void setProgram(Program program) {
    this.program = program;
  }

  public Sequence<Boolean> getRhythmGenerator() {
    return rhythmGenerator;
  }

  public void setRhythmGenerator(Sequence<Boolean> rhythmGenerator) {
    this.rhythmGenerator = rhythmGenerator;
  }

  public Sequence<Integer> getLengthGenerator() {
    return lengthGenerator;
  }

  public void setLengthGenerator(Sequence<Integer> lengthGenerator) {
    this.lengthGenerator = lengthGenerator;
  }

  public Sequence<Integer> getVelocityGenerator() {
    return velocityGenerator;
  }

  public void setVelocityGenerator(Sequence<Integer> velocityGenerator) {
    this.velocityGenerator = velocityGenerator;
  }

}
