package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmpiricalDistributionTest {

  @Test
  public void testIndexFor() {
    assertIndexFor("xx", 0.0, 0);
    assertIndexFor("xx", 0.5, 0);
    assertIndexFor("xx", 1.0, 1);
    assertIndexFor("xx", 1.5, 1);
    
    assertIndexFor("555", 0.0, 0);
    assertIndexFor("555", 0.49, 0);
    assertIndexFor("555", 0.51, 1);
    assertIndexFor("555", 1.1, 2);
    
    assertIndexFor("1", 0.0, 0);

    assertIndexFor("5x", 0.0, 0);
    assertIndexFor("5x", 0.49, 0);
    assertIndexFor("5x", 0.51, 1);
    assertIndexFor("5x", 1.1, 1);
    
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testIllegalIndex() {
    assertIndexFor("1", 1, 0);
  }

  @Test
  public void testRandomNumber() {
    assertRandom("10000", .1);
    assertRandom("01000", .3);
    assertRandom("00500", .5);
    assertRandom("000x0", .7);
    assertRandom("0000x", .9);
  }

  private void assertRandom(String pattern, double expected) {
    EmpiricalDistribution generator = new EmpiricalDistribution(1, pattern);
    double actual = generator.next();
    assertEquals(expected, actual, 0.0001);
  }

  private void assertIndexFor(String pattern, double arg, int expected) {
    EmpiricalDistribution generator = new EmpiricalDistribution(1, pattern);
    assertEquals(expected, generator.indexFor(arg));
  }

}
