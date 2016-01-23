package de.jlab.scales.random;

import java.util.List;

import com.google.common.collect.Lists;

public class Appender<T> extends ContextAwareSequence<T> {

  private List<Sequence<T>> sequences = Lists.newArrayList();

  @SafeVarargs
  public Appender(Sequence<T>... sequences) {
    this(null, sequences);
  }

  @SafeVarargs
  public Appender(Context context, Sequence<T>... sequences) {
    super(context, 0);
    append(sequences);
  }

  @SuppressWarnings("unchecked")
  public void append(Sequence<T>... sequences) {
    for (Sequence<T> sequence : sequences) {
      this.sequences.add(sequence);
      setSize(size() + sequence.size());
    }
  }

  @Override
  public T next() {
    int index = nextIndex();
    for (Sequence<T> sequence : sequences) {
      if (index < sequence.size())
        return sequence.next();
      index -= sequence.size();
    }
    throw new IllegalStateException("could not find sequence (size() has changed?)");
  }

  @Override
  public void reset() {
    super.reset();
    for (Sequence<T> sequence : sequences)
      sequence.reset();
  }

}
