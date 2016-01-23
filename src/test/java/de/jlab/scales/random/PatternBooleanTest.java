package de.jlab.scales.random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatternBooleanTest {

  @Test
  public void testLocalContextPatternGenerator() {
    PatternBoolean generator = new PatternBoolean(".x");
    for (int i = 0; i < 100; i++) {
      assertFalse(generator.next());
      assertTrue(generator.next());
    }
  }

}
