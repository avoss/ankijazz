package com.ankijazz.rhythm;

import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.b3;
import static com.ankijazz.rhythm.Event.b4;
import static com.ankijazz.rhythm.Event.r1;
import static com.ankijazz.rhythm.Event.r2;
import static com.ankijazz.rhythm.Event.r3;
import static com.ankijazz.rhythm.Event.r4;
import static com.ankijazz.rhythm.Event.r6;
import static com.ankijazz.rhythm.Event.r8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class EventTest {

  @Test
  public void testCombinable() {
    for (Event e1 : Arrays.asList(b1,b2,b3,b4,r8)) {
      for (Event e2 : Event.values()) {
        assertFalse(e1.isCombinableWith(e2));
        assertFalse(e2.isCombinableWith(e1));
      }
    }
    assertCombinable(r1,r1,r2);
    assertCombinable(r1,r2,r3);
    assertCombinable(r1,r3,r4);
    assertCombinable(r2,r1,r3);
    assertCombinable(r2,r2,r4);
    assertCombinable(r3,r1,r4);
    assertCombinable(r4,r2,r6);
    assertCombinable(r6,r2,r8);
  }

  private void assertCombinable(Event e1, Event e2, Event expected) {
    assertTrue(e1.isCombinableWith(e2));
    assertTrue(e2.isCombinableWith(e1));
    assertEquals(expected, e1.combineWith(e2));
    assertEquals(expected, e2.combineWith(e1));
  }

}
