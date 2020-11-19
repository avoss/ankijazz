package de.jlab.scales.rhythm;

import java.util.List;

public class BasicRhythm extends AbstractRhythm {

  private final String title;
  private final String type;
  private final boolean syncopated;
  
  public BasicRhythm(List<Quarter> quarters) {
    super(quarters);
    this.syncopated = quarters.stream().filter(Quarter::isSyncopated).findAny().isPresent();
    this.title = syncopated ? "Syncopated Building Blocks" : "Basic Building Blocks";
    this.type = syncopated ? "Syncopated" : "Basic";
    
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getTypeName() {
    return type;
  }
}
