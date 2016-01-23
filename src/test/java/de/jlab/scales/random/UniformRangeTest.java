package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class UniformRangeTest {

  @Test
  public void whenRangeIsZeroShouldReturnConstantNumber() {
    assertRange(42, 42, Sets.newHashSet(42));
  }

  @Test
  public void shouldReturnNumbersInRange1() {
    assertRange(41, 43, Sets.newHashSet(41, 42, 43));
  }

  @Test
  public void shouldReturnNumbersInRange2() {
    assertRange(40, 44, Sets.newHashSet(40, 41, 42, 43, 44));
  }

  private void assertRange(int lowerBound, int upperBound, Set<Integer> expected) {
    UniformRange g = new UniformRange(lowerBound, upperBound);
    Set<Integer> actual = Sets.newHashSet();
    for (int i = 0; i < 1000; i++)
      actual.add(g.next());
    assertEquals(expected, actual);
  }

}
