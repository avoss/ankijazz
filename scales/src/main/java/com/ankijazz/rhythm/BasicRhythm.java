package com.ankijazz.rhythm;

import java.util.List;

public class BasicRhythm extends AbstractRhythm {

  public enum Type {
    BASIC("Basic"),
    SYNCOPATED("Syncopated"),
    TIED("Tied");

    private String label;

    Type(String label) {
      this.label = label;
    }
    
    public String getLabel() {
      return label;
    }
  }
  
  private final String title;
  private final String typeName;
  
  public BasicRhythm(Type type, List<Quarter> quarters) {
    super(quarters);
    this.title = String.format("%s Building Blocks", type.getLabel());
    this.typeName = type.getLabel();
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }
}
