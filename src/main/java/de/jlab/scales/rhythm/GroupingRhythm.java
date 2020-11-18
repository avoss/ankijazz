package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GroupingRhythm extends AbstractRhythm {


  private int length;
  private int position;

  protected GroupingRhythm(int length, int position, List<EventSequence> sequences) {
    super(sequences);
    this.length = length;
    this.position = position;
  }

  @Override
  public String getTitle() {
    return String.format("Group of length %d, position %d", length, position + 1);
  }

  @Override
  public String getTypeName() {
    return "Grouping";
  }

  public GroupingRhythm transpose() {
    Function<List<EventSequence>, GroupingRhythm> factory = seqs -> {
      return new GroupingRhythm(length, (position + 1) % length, seqs);
    };
    return super.transpose(factory);
  }

}
