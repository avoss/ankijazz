package de.jlab.scales.rhythm;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class EventSequencesTest {

  @Test
  public void testEventSequences() {
    EventSequences eventSequences = new EventSequences();
    TestUtils.assertFileContentMatches(eventSequences.toString(), getClass(), "EventSequences.txt");
  }

}
