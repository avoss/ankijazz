package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Quarter.q;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class RhythmTest {

  @Test
  public void testTies() {
    Quarter q1 = q(b2,b2).tie();
    Quarter q2 = q(b2,b2);
    Quarter q3 = q(b2,b2);
    RandomRhythm rhythm = new RandomRhythm(List.of(q1,q2,q3));
    assertTrue(rhythm.hasTies());
    assertEquals("b2b2 ~ b2b2 b2b2", rhythm.toString());
  }

}
