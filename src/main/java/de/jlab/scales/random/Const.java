package de.jlab.scales.random;

public class Const<T> extends AbstractSequence<T> {

  private T value;

  public Const(T value) {
    this(1, value);
  }
  
  public Const(int size, T value) {
    super(size);
    this.value = value;
  }

  @Override
  public T next() {
    return value;
  }

}
