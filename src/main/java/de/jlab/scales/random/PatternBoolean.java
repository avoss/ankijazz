package de.jlab.scales.random;

import java.util.Random;

public class PatternBoolean extends ContextAwareSequence<Boolean> {

  private double[] probabilities;
  private Random random = new Random();

  public PatternBoolean(Context context, String pattern) {
    super(context, 0);
    probabilities = Patterns.parse(pattern);
    setSize(probabilities.length);
  }
  
  public PatternBoolean(String pattern) {
    this(null, pattern);
  }

  @Override
  public Boolean next() {
    return random.nextDouble() < probabilities[nextIndex()];
  }

  public int size() {
    return probabilities.length;
  }

}
