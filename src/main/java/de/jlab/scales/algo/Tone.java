package de.jlab.scales.algo;

/**
 * unit to be copied, repeated, tranformed.
 * 
 * TODO: whole sections with multiple instruments have to be copied, repeated, transformed. 
 * So this ist probably not the right abstraction.
 */
public class Tone {
  int time;
  int pitch; // maybe scale degree or midi pitch
  int length;
  int velocity;

}
