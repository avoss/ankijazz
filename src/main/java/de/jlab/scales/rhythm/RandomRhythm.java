package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Set;

public class RandomRhythm extends AbstractRhythm {

  public RandomRhythm(List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    super(sequences, tiedSequences);
  }

  @Override
  public String getTitle() {
    return String.format("Random rhythm with %d pattern(s)", getUniqueSequences().size());
  }

  @Override
  public String getTypeName() {
    return "Random";
  }

}
