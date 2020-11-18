package de.jlab.scales.rhythm;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class QuarterGeneratorTest {

  @Test
  public void testEventSequences() {
    QuarterGenerator generator = new QuarterGenerator();
    TestUtils.assertFileContentMatches(generator.toString(), getClass(), "QuarterGenerator.txt");
  }

}
