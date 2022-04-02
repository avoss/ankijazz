package com.ankijazz.anki;

public enum FretboardPosition {
  
  LOW("Low", "AnkiJazz-FretboardLow.png"),
  MEDIUM("Medium", "AnkiJazz-FretboardMedium.png"),
  HIGH("High", "AnkiJazz-FretboardHigh.png");

  private final String label;
  private final String image;

  FretboardPosition(String label, String image) {
    this.label = label;
    this.image = image;
  }

  public String getLabel() {
    return label;
  }
  
  public String getImage() {
    return image;
  }
}
