package de.jlab.scales.random;

import org.junit.Test;

public class RangeConverterTest {

  @Test
  public void test() {
    FixedSequence<Double> source = new FixedSequence<Double>(0.0, 0.04, 0.05001, 0.06, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99);
    RangeConverter sequence = new RangeConverter(source, 0, 10);
    TestUtil.assertInteger(sequence, 0, 0, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

}
