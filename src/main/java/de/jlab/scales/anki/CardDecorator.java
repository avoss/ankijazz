package de.jlab.scales.anki;

import java.nio.file.Path;

import de.jlab.scales.Utils;

public class CardDecorator implements Card {

  private final Card delegate;
  private final String[] extraFields;
  
  public CardDecorator(Card delegate, String ... extraFields) {
    this.delegate = delegate;
    this.extraFields = extraFields;
  }

  public int getDifficulty() {
    return delegate.getDifficulty();
  }

  public void writeAssets(Path directory) {
    delegate.writeAssets(directory);
  }

  public int compareTo(Card that) {
    return delegate.compareTo(that);
  }

  @Override
  public String toCsv() {
    return delegate.toCsv() + ";" + Utils.toCsv(extraFields);
  }

}
