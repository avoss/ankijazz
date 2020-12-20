package de.jlab.scales.midi;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public class HumanizingMidiOut implements MidiOut {
  
  private final MidiOut delegate;
  private final int randomTicks;

  public HumanizingMidiOut(MidiOut delegate) {
    this(delegate, 6); // TODO depends on MidiFile.PPQ
  }
  
  public HumanizingMidiOut(MidiOut delegate, int randomTicks) {
    this.delegate = delegate;
    this.randomTicks = randomTicks;
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
    int originalClock = delegate.getClock();
    int humanizedClock = originalClock + ThreadLocalRandom.current().nextInt(2 * randomTicks) - randomTicks;
    if (humanizedClock > 0) {
      delegate.setClock(humanizedClock);
    }
    delegate.noteOn(channel, pitch, velocity, numerator, denominator);
    delegate.setClock(originalClock);
  }

  public void programChange(int channel, int program) {
    timely(() -> delegate.programChange(channel, program));
  }

  public void controllerChange(int channel, int controller, int value) {
    timely(() -> delegate.controllerChange(channel, controller, value));
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

  /**
   * keep order of e.g. program change event when noteOn was randomly time-shifted. 
   * Runnable must not change clock.
   */
  private void timely(Runnable statement) {
    int originalClock = delegate.getClock();
    if (originalClock > randomTicks) {
      delegate.setClock(originalClock - randomTicks);
    }
    statement.run();
    delegate.setClock(originalClock);
  }

}
