package com.ankijazz.rhythm;

import java.util.List;
import java.util.function.Function;

public class GroupingRhythm extends AbstractRhythm {
  private int length;
  private int position;

  public GroupingRhythm(int length, List<Quarter> quarters) {
    this(length, 0, quarters);
  }
  
  protected GroupingRhythm(int length, int position, List<Quarter> quarters) {
    super(quarters);
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

  public GroupingRhythm transpose(int distance) {
    Function<List<Quarter>, GroupingRhythm> factory = quarters -> {
      return new GroupingRhythm(length, (position + getLength() + distance) % getLength(), quarters);
    };
    return super.transpose(distance, factory);
  }

}
