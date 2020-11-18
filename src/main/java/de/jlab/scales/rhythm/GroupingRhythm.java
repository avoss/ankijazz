package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Set;

public class GroupingRhythm extends AbstractRhythm {

  private String type;
  private int group;

  protected GroupingRhythm(String type, int group, List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    super(sequences, tiedSequences);
    this.type = type;
    this.group = group;
  }

  @Override
  public String getTitle() {
    return String.format("%s (group %d)", type, group);
  }

  @Override
  public String getTypeName() {
    return "Grouping";
  }

}
