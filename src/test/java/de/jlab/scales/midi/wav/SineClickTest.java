package de.jlab.scales.midi.wav;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SineClickTest {
  
  private static final double EPS = 0.00001;

  @Test
  public void testFreq() {
    assertEquals(440.0, SineClick.midi2Hz(69), EPS);
    assertEquals(8.1757989156, SineClick.midi2Hz(0), EPS);
    assertEquals(12543.8539514160, SineClick.midi2Hz(127), EPS);
    assertEquals(246.9416506281, SineClick.midi2Hz(59), EPS);
  }

}
