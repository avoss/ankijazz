package de.jlab.scales.random;

public abstract class ContextAwareSequence<T> extends AbstractSequence<T> {

  private ContextImpl localContext = new ContextImpl();
  private Context globalContext = localContext;
  
  protected ContextAwareSequence(Context globalContext, int size) {
    super(size);
    if (globalContext != null)
      this.globalContext = globalContext;
  }

  /**
   * return position w/o increment
   */
  protected int getPosition() {
    return globalContext.getPosition();
  }

  /**
   * return index and increment current position
   */
  protected int nextIndex() {
    int position = getPosition();
    localContext.next();
    return position % size();
  }

  @Override
  public void reset() {
    super.reset();
    localContext.reset();
  }

}
