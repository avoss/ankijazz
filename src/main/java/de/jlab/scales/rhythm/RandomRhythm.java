package de.jlab.scales.rhythm;

import java.util.List;
import java.util.Set;

public class RandomRhythm extends AbstractRhythm {

  public RandomRhythm(List<Quarter> quarters) {
    super(quarters);
  }

  @Override
  public String getTitle() {
    return String.format("Random rhythm with %d pattern(s)", getUniqueQuarters().size());
  }

  @Override
  public String getTypeName() {
    return "Random";
  }

}
