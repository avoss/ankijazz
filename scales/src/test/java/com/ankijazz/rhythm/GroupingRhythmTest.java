package com.ankijazz.rhythm;

import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.b3;
import static com.ankijazz.rhythm.Event.r2;
import static com.ankijazz.rhythm.Quarter.q;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class GroupingRhythmTest {

  @Test
  public void testTranspose() {
    Quarter q1 = q(b3, b1).tie();
    Quarter q2 = q(b2, b2).tie();
    Quarter q3 = q(b1, b3);
    Quarter q4 = q(b1, r2, b1);
    GroupingRhythm rhythm = new GroupingRhythm(3, 0, List.of(q1, q2, q3, q4));
    assertEquals("Group of length 3, position 1", rhythm.getTitle());
    assertEquals("b3b1 ~ b2b2 ~ b1b3 b1r2b1", rhythm.toString());
    //            r1b1b2 ~ b1b3 b3b1 ~ b2b1r1
    rhythm = rhythm.transpose(2);
    assertEquals("Group of length 3, position 3", rhythm.getTitle());
    assertEquals("r1b1b2 ~ b1b3 b3b1 ~ b2b1r1", rhythm.toString());
//    rhythm = rhythm.transpose();
//    assertEquals("r1b1b2 ~ b1b3 b3b1 ~ b2b2", rhythm.toString());
//    rhythm = rhythm.transpose();
//    assertEquals("b3b1 ~ b2b2 ~ b1b3 b3b1", rhythm.toString());
  }

}
