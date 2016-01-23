package de.jlab.scales.random;

import java.util.Random;

/**
 * returns a uniform distributed number in a given range (inclusive)
 */
public class UniformRange extends AbstractSequence<Integer> {

  private final Random random = new Random();
  private final int lowerBound;
  private final int range;

  public UniformRange(int lowerBound, int upperBound) {
    this(1, lowerBound, upperBound);
  }

  public UniformRange(int size, int lowerBound, int upperBound) {
    super(size);
    this.lowerBound = lowerBound;
    this.range = upperBound - lowerBound + 1;
  }

  @Override
  public Integer next() {
    return lowerBound + random.nextInt(range);
  }

}
