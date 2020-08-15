package de.jlab.scales.anki;

public class BasicCard implements Card {
  private final double priority;
  private final String[] fields;

  public BasicCard(double priority, String[] fields) {
    this.priority = priority;
    this.fields = fields;
  }

  @Override
  public double getPriority() {
    return priority;
  }

  @Override
  public String[] getFields() {
    return fields;
  }
}
