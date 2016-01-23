package de.jlab.scales.random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecorderTest {

  @Test
  public void test() {
    Recorder<Boolean> recorder = new Recorder<Boolean>(new PatternBoolean(".5555555555555x"));
    assertEquals(15, recorder.size());
    boolean[] expected = new boolean[recorder.size()];
    for (int i = 0; i < recorder.size(); i++)
      expected[i] = recorder.next();
    TestUtil.assertBoolean(recorder.playback(), expected);
  }

}
