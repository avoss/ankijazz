package de.jlab.scales.midi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class MockMidiOut implements MidiOut {

  private int clock;
  private Program program;
  private List<NoteOn> notes = new ArrayList<>();
  private Tempo tempo;
  private TimeSignature timeSignature;
  private int volume;
  private int pan;

  @Override
  public void setTimeSignature(int numerator, int denominator) {
    this.timeSignature = Parts.timeSignature(numerator, denominator);
  }

  @Override
  public void setTempo(int tempo) {
    this.tempo = Parts.tempo(tempo);
  }

  @Override
  public void advance(int numerator, int denominator) {
    clock += MidiFile.ticks(numerator, denominator);
  }

  @Override
  public void noteOn(int channel, int pitch, int velocity, int numerator, int denominator) {
    notes.add(new NoteOn(channel, pitch, velocity, numerator, denominator));
  }

  @Override
  public void programChange(int channel, int midiProgram) {
    this.program = Arrays.stream(Program.values()).filter(p -> p.getMidiProgram() == midiProgram).findAny().orElseThrow();
  }

  @Override
  public void controllerChange(int channel, int controller, int value) {
    switch (controller) {
    case 7: 
      this.volume = value;
      break;
    case 10:
      this.pan = value;
      break;
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

  @Override
  public void save(Path path) {
    
  }

}
