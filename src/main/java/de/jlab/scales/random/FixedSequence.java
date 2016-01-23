package de.jlab.scales.random;

/**
 * returns fixed objects in order
 */
public class FixedSequence<T> extends ContextAwareSequence<T> {
  private T[] values;

  @SafeVarargs
  public FixedSequence(T... values) {
    this(null, values);
  }

  @SafeVarargs
  public FixedSequence(Context context, T... values) {
    super(context, values.length);
    this.values = values;
  }

  @Override
  public T next() {
    return values[nextIndex()];
  }

}
