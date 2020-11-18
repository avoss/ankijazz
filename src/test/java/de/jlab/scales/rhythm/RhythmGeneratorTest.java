package de.jlab.scales.rhythm;

import java.util.List;

import org.junit.Test;

public class RhythmGeneratorTest {

  @Test
  public void test() {
    // FIXME add asserts
    RhythmGenerator generator = new RhythmGenerator();
    List<AbstractRhythm> rhythms = generator.generate();
    for (AbstractRhythm rhythm : rhythms) {
      for (EventSequence sequence: rhythm.getSequences()) {
       // assertEquals(new Fraction(4), sequence.getLength());
      }
      System.out.println(rhythm.getDifficulty());
    }
  }

}
