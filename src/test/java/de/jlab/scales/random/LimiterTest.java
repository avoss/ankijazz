package de.jlab.scales.random;

import org.junit.Test;

public class LimiterTest {

  @Test
  public void testLimiterLocalContext() {
    testLimiter("xx..", ".", true, false, false, false, true);
  }

  @Test
  public void testLimiterGlobalContext() {
    ContextImpl context = new ContextImpl();
    PatternBoolean master = new PatternBoolean(context, "xx..");
    Limiter limiter = new Limiter(".", master);
    TestUtil.assertBoolean(context, limiter, true, false, false, false, true);
  }

  @Test
  public void testLimiter2() {
    testLimiter(".xxx", ".xx", false, true, false, true, false, true);
  }
  
  private void testLimiter(String masterPattern, String limiterPattern,  boolean ... expected) {
    PatternBoolean master = new PatternBoolean(masterPattern);
    Limiter limiter = new Limiter(limiterPattern, master);
    TestUtil.assertBoolean(limiter, expected);
  }


}
