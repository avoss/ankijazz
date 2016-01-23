package de.jlab.scales.random;

public abstract class AbstractSequence<T> implements Sequence<T> {
  private int size;

  protected AbstractSequence(int size) {
    this.size = size;
  }

  @Override
  public void reset() {
  }

  @Override
  public int size() {
    return size;
  }

  protected void setSize(int size) {
    this.size = size;
  }

}
