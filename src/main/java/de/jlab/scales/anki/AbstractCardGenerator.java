package de.jlab.scales.anki;

import java.util.Collection;
import java.util.Iterator;

import de.jlab.scales.Utils.LoopIteratorFactory;

public abstract class AbstractCardGenerator<T extends Card> implements CardGenerator<T> {

  private final String title;
  private final String fileName;
  private LoopIteratorFactory iteratorFactory;

  protected AbstractCardGenerator(LoopIteratorFactory iteratorFactory, String title, String fileName) {
    this.iteratorFactory = iteratorFactory;
    this.title = title;
    this.fileName = fileName;
  }
  
  @Override
  public String getFileName() {
    return fileName;
  }
  
  @Override
  public String getTitle() {
    return title;
  }
  
  protected <U> Iterator<U> loopIterator(Collection<? extends U> collection) {
    return iteratorFactory.iterator(collection);
  }
  
}
