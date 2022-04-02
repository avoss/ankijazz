package de.jlab.scales.midi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.google.common.annotations.VisibleForTesting;

public class MidiFile implements MidiOut {
  // http://www.jsresources.org
  // similar project http://www.jfugue.org/
  private Sequence sequence;
  private Track track;
  
  private static final int PPQ = 480;
  
  private int clock = 0;

  public MidiFile() {
    try {
      sequence = new Sequence(Sequence.PPQ, PPQ);
      track = sequence.createTrack();
    } catch (InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void setTimeSignature(int numerator, int denominator) {
    try {
      MetaMessage mm = new MetaMessage();
      byte[] data = new byte[4];
      data[0] = (byte) numerator;
      data[1] = (byte) Math.round(Math.log(denominator) / Math.log(2.0));
      data[2] = 24; // number of MIDI clocks in a metronome click
      data[3] = 8;  // number of notated 32nd-notes in a MIDI quarter-note (24 MIDI Clocks)
      mm.setMessage(0x58, data, 4);
      track.add(new MidiEvent(mm, clock));
    } catch (InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void setTempo(int bpm) {
    try {
      MetaMessage mm = new MetaMessage();
      long microsecondsPerQuarterNote = 60000000L / (long)bpm;
      byte[] data = new byte[3];
      data[0] = (byte)((microsecondsPerQuarterNote >> 16) & 0x0000ffL);
      data[1] = (byte)((microsecondsPerQuarterNote >> 8) & 0x0000ffL);
      data[2] = (byte)((microsecondsPerQuarterNote) & 0x0000ffL);
      mm.setMessage(0x51, data, 3);
      track.add(new MidiEvent(mm, clock));
    } catch (InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void addLyrics(String text) {
    try {
      byte[] data = text.getBytes();
      MetaMessage mm = new MetaMessage();
      mm.setMessage(0x05, data, data.length);
      track.add(new MidiEvent(mm, clock));
    } catch (InvalidMidiDataException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void advance(int numerator, int denominator) {
    clock += ticks(numerator, denominator);
  }

  @VisibleForTesting
  static int ticks(int numerator, int denominator) {
    return PPQ * 4 * numerator / denominator;
  }
  
  @Override
  public void noteOn(int channel, int pitch, int velocity, int numerator, int denominator) {
    track.add(shortMessage(clock, ShortMessage.NOTE_ON, channel, pitch, velocity));
    track.add(shortMessage(clock + ticks(numerator, denominator) - 1, ShortMessage.NOTE_OFF, channel, pitch, 0));
  }
  
  @Override
  public void programChange(int channel, int program) {
    track.add(shortMessage(clock, ShortMessage.PROGRAM_CHANGE, channel, program, 0));
  }

  public void controllerChange(int channel, int controller, int value) {
    track.add(shortMessage(clock, ShortMessage.CONTROL_CHANGE, channel, controller, value));
  }

  /**
   * 0 = left, 64 = center, 127 = right
   */
  public void panChange(int channel, int value) {
    track.add(shortMessage(clock, ShortMessage.CONTROL_CHANGE, channel, 10, value));
  }
  
  @Override
  public void save(Path path) {
    try {
      File file = path.toFile();
      file.getParentFile().mkdirs();
      MidiSystem.write(sequence, 0, file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public byte[] getBytes() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      MidiSystem.write(sequence, 0, bos);
      return bos.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private MidiEvent shortMessage(long clock, int status, int channel, int data1,  int data2) {
    try {
      ShortMessage sm = new ShortMessage();
      sm.setMessage(status, channel, data1, data2);
      return new MidiEvent(sm, clock);
    } catch (InvalidMidiDataException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public int getClock() {
    return clock;
  }

  @Override
  public void setClock(int clock) {
    this.clock = clock;
  }
  
}
