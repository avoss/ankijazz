package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;

public class RepeaterTest {

  @Test
  public void testRepeater() {
    Sequence<Integer> source = new UniformRange(16, 1, 100);
    Repeater<Integer> repeater = new Repeater<Integer>(source, 2);
    List<Integer> expected = TestUtil.collectSequence(source.size(), repeater);
    assertEquals(expected,  TestUtil.collectSequence(source.size(), repeater));
    assertEquals(expected,  TestUtil.collectSequence(source.size(), repeater));
    assertNotEquals(expected,  TestUtil.collectSequence(source.size(), repeater));
  }


}
