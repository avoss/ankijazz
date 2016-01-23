package de.jlab.scales.random;

import java.util.Random;

public class ChooseAny<T> extends AbstractSequence<T> {

  private T[] values;
  private Random random = new Random();

  @SafeVarargs
  public ChooseAny(T ... values) {
    super(values.length);
    this.values = values;
  }
  
  @Override
  public T next() {
    return values[random.nextInt(values.length)];
  }

}
