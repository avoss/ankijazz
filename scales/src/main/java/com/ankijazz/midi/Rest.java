package com.ankijazz.midi;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Rest  extends AbstractPart  {
  public static final Rest r1 = new Rest(1, 1);
  public static final Rest r2 = new Rest(1, 2);
  public static final Rest r4 = new Rest(1, 4);
  public static final Rest r8 = new Rest(1, 8);
  public static final Rest r16 = new Rest(1, 16);
  
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
