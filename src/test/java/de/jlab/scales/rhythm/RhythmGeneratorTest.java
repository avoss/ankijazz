package de.jlab.scales.rhythm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class RhythmGeneratorTest {

  @Test
  public void test() {
    // FIXME add asserts
    RhythmGenerator generator = new RhythmGenerator(new EventSequences().getEventSequenceMap());
    List<Rhythm> rhythms = generator.generate();
    for (Rhythm rhythm : rhythms) {
      System.out.println(rhythm.getDifficulty());
    }
  }

}
