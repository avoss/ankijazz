package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  public String[] getFields() {
    List<String> result = new ArrayList<>();
    result.addAll(Arrays.asList(delegate.getFields()));
    result.addAll(Arrays.asList(extraFields));
    return result.toArray(new String[result.size()]);
  }

  public void writeAssets(Path directory) {
    delegate.writeAssets(directory);
  }

  public int compareTo(Card that) {
    return delegate.compareTo(that);
  }

}
