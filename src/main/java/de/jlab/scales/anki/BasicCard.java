package de.jlab.scales.anki;

public class BasicCard implements Card {
  private final int priority;
  private final String[] fields;

  public BasicCard(int priority, String[] fields) {
    this.priority = priority;
    this.fields = fields;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public String[] getFields() {
    return fields;
  }

}
