package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.*;
import static de.jlab.scales.rhythm.EventSequence.q;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class RhythmTest {

  @Test
  public void testTies() {
    EventSequence q1 = q(b2,b2).tie();
    EventSequence q2 = q(b2,b2);
    EventSequence q3 = q(b2,b2);
    RandomRhythm rhythm = new RandomRhythm(List.of(q1,q2,q3));
    assertTrue(rhythm.hasTies());
    assertEquals("b2b2 ~ b2b2 b2b2", rhythm.toString());
  }

}
