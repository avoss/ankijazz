package de.jlab.scales.random;

public class ContextImpl implements Context {

  private int position = 0;

  @Override
  public int getPosition() {
    return position;
  }

  public void next() {
    position += 1;
  }

  public void reset() {
    position = 0;
  }

}
