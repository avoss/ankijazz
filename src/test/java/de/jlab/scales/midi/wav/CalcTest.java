package de.jlab.scales.midi.wav;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CalcTest {
  private Calc calc;
  private final int samplingRate = 1440;

  @Before
  public void setUp() {
    calc = new Calc(samplingRate, 120);
  }

  @Test
  public void testSize() {
    assertSize(samplingRate, 1, 2);
    assertSize(2 * samplingRate, 1, 1);
    assertSize(samplingRate / 8, 1, 16);
    assertSize(samplingRate * 3 / 8, 3, 16);
  }

  private void assertSize(int expected, int nominator, int denominator) {
    int actual = calc.samples(nominator, denominator);
    assertEquals(expected, actual);
  }

}
