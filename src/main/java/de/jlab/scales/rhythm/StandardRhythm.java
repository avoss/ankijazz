package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Set;

public class StandardRhythm extends AbstractRhythm {

  private String title;

  public StandardRhythm(String title, List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    super(sequences, tiedSequences);
    this.title = title;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getTypeName() {
    return "Standard";
  }
}
