package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.*;
import static de.jlab.scales.rhythm.EventSequence.q;
import static de.jlab.scales.rhythm.Event.b3;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class GroupingRhythmTest {

  @Test
  public void testTranspose() {
    EventSequence q1 = q(b3, b1).tie();
    assertEquals("b3b1 ~", q1.toString());
    EventSequence q2 = q(b2, b2).tie();
    EventSequence q3 = q(b1, b3);
    EventSequence q4 = q(b3, b1);
    GroupingRhythm rhythm = new GroupingRhythm(4, 0, List.of(q1, q2, q3, q4));
    assertEquals("b3b1 ~ b2b2 ~ b1b3 b3b1", rhythm.toString());
    rhythm = rhythm.transpose();
//    assertEquals("b1b3 b3b1 ~ b2b2 ~ b1b3", rhythm.toString());
//    rhythm = rhythm.transpose();
//    assertEquals("r1b1b2 ~ b1b3 b3b1 ~ b2b2", rhythm.toString());
//    rhythm = rhythm.transpose();
//    assertEquals("b3b1 ~ b2b2 ~ b1b3 b3b1", rhythm.toString());
  }

}
