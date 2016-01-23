package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AppenderTest {
  private Appender<Integer> appender;

  @Before
  public void setUp() {
    Sequence<Integer> a = new FixedSequence<Integer>(1, 2);
    Sequence<Integer> b = new FixedSequence<Integer>(3, 4, 5);
    appender = new Appender<Integer>(a, b);
    assertEquals(5, appender.size());
  }
  
  @Test
  public void test() {
    TestUtil.assertInteger(appender, 1,2,3,4,5,1);
  }

  
  @Test
  public void testReset() {
    TestUtil.assertInteger(appender, 1);
    appender.reset();
    TestUtil.assertInteger(appender, 1,2,3,4);
    appender.reset();
    TestUtil.assertInteger(appender, 1,2,3,4,5,1);
    
  }

}
