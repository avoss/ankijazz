package de.jlab.scales.midi.wav;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import de.jlab.scales.midi.MidiOut;

public class WavFile implements MidiOut {
  private final int SAMPLING_RATE = 44100;
  private Calc calc = new Calc(SAMPLING_RATE, 120);
  private SampleBuffer buffer;

  public WavFile() {
    buffer = new SampleBuffer(SAMPLING_RATE);
  }
  
  @Override
  public void setTimeSignature(int numerator, int denominator) {
  }

  @Override
  public void setTempo(int beatsPerMinute) {
    calc = new Calc(SAMPLING_RATE, beatsPerMinute);
  }

  @Override
  public void advance(int numerator, int denominator) {
    buffer.setPosition(buffer.getPosition() + calc.samples(numerator, denominator));
  }

  @Override
  public void noteOn(int channel, int midiPitch, int velocity, int numerator, int denominator) {
    short[] samples = new SineClick(SAMPLING_RATE).noteOn(midiPitch, velocity);
    buffer.add(samples);
  }

  @Override
  public void programChange(int channel, int program) {
  }
  
  @Override
  public void controllerChange(int channel, int controller, int value) {
  }
  
  @Override
  public int getClock() {
    return buffer.getPosition();
  }

  @Override
  public void setClock(int clock) {
    buffer.setPosition(clock);
  }

  @Override
  public void save(Path path) {
    try {
      File file = path.toFile();
      file.getParentFile().mkdirs();
      AudioSystem.write(buffer.toAudioInputStream(), AudioFileFormat.Type.WAVE, file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
