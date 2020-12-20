package de.jlab.scales.midi;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TimeSignature extends AbstractPart {
  final int numerator;
  final int denominator;
  
  public TimeSignature(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.setTimeSignature(numerator, denominator);
  }

}
