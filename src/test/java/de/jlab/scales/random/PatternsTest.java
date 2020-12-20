package de.jlab.scales.random;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PatternsTest {

  private String pattern;
  private double[] expected;

  @Parameters(name = "pattern = {0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] { 
        { "", new double[0] }, 
        { "  ", new double[0] },
        { "0123456789x", new double[]{0,.1,.2,.3, .4, .5, .6, .7, .8, .9, 1} },
        { "x...", new double[]{1,0,0,0} },
        { "|x...  |  x...|", new double[]{1,0,0,0,   1,0,0,0} },
        { "1 2", new double[]{.1,.2} },
        { "x--.", new double[]{1,-1, -1, 0} },
    });
  }

  public PatternsTest(String pattern, double[] expected) {
    this.pattern = pattern;
    this.expected = expected;
    
  }

  @Test
  public void testParsePattern() {
    Assert.assertArrayEquals(pattern, expected, Patterns.parse(pattern), .0001);
  }

}
