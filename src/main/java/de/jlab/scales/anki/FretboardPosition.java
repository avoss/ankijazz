package de.jlab.scales.anki;

public enum FretboardPosition {
  
  LOW("Low", "<img src=\"AnkiJazz-FretboardLow.png\">"),
  MEDIUM("Medium", "<img src=\"AnkiJazz-FretboardMedium.png\">"),
  HIGH("High", "<img src=\"AnkiJazz-FretboardHigh.png\">");

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
