package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class NoteOn extends AbstractPart {
  private final int channel;
  private final int pitch;
  private final int velocity;
  private final int numerator; 
  private final int denominator;

  public NoteOn(int channel, int pitch, int velocity, int numerator, int denominator) {
    this.channel = channel;
    this.pitch = pitch;
    this.velocity = velocity;
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  @Override
  public void perform(MidiOut midiOut) {
    midiOut.noteOn(channel, pitch, velocity, numerator, denominator);
  }

}
