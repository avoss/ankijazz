package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

public class BR80 {

  // metronome not implemented!
//  public static final int metronomeClick = 33;
//  public static final int metronomeBell = 34;
  public static final int tom4 = 41;
  public static final int tom3 = 45;
  public static final int tom2 = 48;
  public static final int tom1 = 50;
  public static final int cowbell = 56;
  public static final int stick = 31;
  public static final int ride = 51;
  public static final int crash = 49;
  public static final int openHihat = 46;
  public static final int closedHihat = 42;
  public static final int snare = 38;
  public static final int kick = 36;

  protected Part r = rest(16);
  protected Part hi = seq(note(9, cowbell, 127, 24), r);
  protected Part lo = seq(note(9, cowbell, 127, 24), r);
  protected Part X = note(9, kick, 127, 24);

}
