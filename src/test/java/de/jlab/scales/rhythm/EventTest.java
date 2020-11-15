package de.jlab.scales.rhythm;

import static de.jlab.scales.rhythm.Event.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.jlab.scales.rhythm.Event;

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
