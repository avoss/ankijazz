package de.jlab.scales.random;

public class Recorder<T> extends ContextAwareSequence<T> {

  private Sequence<T> delegate;
  private T[] recordedValues;
  private Context context;

  public Recorder(Sequence<T> delegate) {
    this(null, delegate);
  }

  @SuppressWarnings("unchecked")
  public Recorder(Context context, Sequence<T> delegate) {
    super(context, delegate.size());
    this.context = context;
    this.delegate = delegate;
    this.recordedValues = (T[])new Object[size()];
  }

  @Override
  public T next() {
    T value = delegate.next();
    recordedValues[nextIndex()] = value;
    return value;
  }
  
  public Sequence<T> playback() {
    return new FixedSequence<T>(context, recordedValues);
  }
  
  @Override
  public void reset() {
    super.reset();
    delegate.reset();
  }

}
