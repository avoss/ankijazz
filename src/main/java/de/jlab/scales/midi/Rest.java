package de.jlab.scales.midi;

public class Rest  extends AbstractPart  {
  private final int numerator;
  private final int denominator;

  public Rest(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.advance(numerator, denominator);
  }
}
