package de.jlab.scales.random;

public abstract class Converter<T, U> implements Sequence<T> {
 
  private final Sequence<U> delegate;

  protected Converter(Sequence<U> delegate) {
    this.delegate = delegate;
  }

  public T next() {
    return convert(delegate.next());
  }

  protected abstract T convert(U dice);

  public void reset() {
    delegate.reset();
  }
  
  @Override
  public int size() {
    return delegate.size();
  }

}
