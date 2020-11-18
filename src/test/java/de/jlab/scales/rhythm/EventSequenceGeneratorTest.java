package de.jlab.scales.rhythm;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class EventSequenceGeneratorTest {

  @Test
  public void testEventSequences() {
    EventSequenceGenerator generator = new EventSequenceGenerator();
    TestUtils.assertFileContentMatches(generator.toString(), getClass(), "EventSequenceGenerator.txt");
  }

}
