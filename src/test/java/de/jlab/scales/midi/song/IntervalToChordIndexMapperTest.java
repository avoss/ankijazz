package de.jlab.scales.midi.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntervalToChordIndexMapperTest {

  @Test
  public void test() {
    IntervalToChordIndexMapper mapper = new IntervalToChordIndexMapper();
    assertEquals(0, mapper.map(1));
    assertEquals(1, mapper.map(3));
    assertEquals(2, mapper.map(5));
    assertEquals(3, mapper.map(7));
    assertEquals(4, mapper.map(9));
  }

}
