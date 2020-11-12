package de.jlab.scales.anki;

public enum Direction {

  ASCENDING("Ascending"), DESCENDING("Descending");

  private String label;

  Direction(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

}
