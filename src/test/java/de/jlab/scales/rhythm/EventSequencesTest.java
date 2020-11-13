package de.jlab.scales.rhythm;

import java.util.Arrays;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class EventSequencesTest {

  @Test
  public void testQuarters() {
    EventSequences eventSequences = new EventSequences();
    TestUtils.assertFileContentMatches(Arrays.asList(eventSequences.toString()), getClass(), "EventSequences.txt");
  }

}
