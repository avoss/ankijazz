package de.jlab.scales.random;

import java.util.Random;

/**
 * returns doubles in range [0..1) from empirical distribution in pattern
 */
public class EmpiricalDistribution extends AbstractSequence<Double> {

  private double[] values;
  private double sum;
  private Random random = new Random();

  public EmpiricalDistribution(int size, String pattern) {
    super(size);
    values = Patterns.parse(pattern);
    for (double value : values)
      sum += value;
  }

  @Override
  public Double next() {
    double value = random.nextDouble() * sum;
    // add 0.5 to index to meet the "middle" of the index
    double result = (indexFor(value) + 0.5) / (double)values.length;
    return result;
  }

  int indexFor(double value) {
    for (int i = 0; i < values.length; i++) {
      value -= values[i];
      if (value < 0)
        return i;
    }
    throw new IllegalArgumentException("can not determine index for " + value);
  }

}
