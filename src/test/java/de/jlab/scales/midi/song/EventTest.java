package de.jlab.scales.midi.song;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EventTest {

  /**
   * same event pattern (e.g. "x...")  may be used in multiple places
   */
  @Test
  public void testEquals() {
    Event e1 = Event.builder().patternId(1).build();
    Event e2 = Event.builder().patternId(2).build();
    assertNotEquals(e1, e2);
  }

}
