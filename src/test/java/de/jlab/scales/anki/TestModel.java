package de.jlab.scales.anki;

public class TestModel implements WithDifficulty, WithAssets {

  private final String front = "A < B?";
  private final String back = "Yes";
  private final String tags = "A B";

  public String getFront() {
    return front;
  }

  public String getBack() {
    return back;
  }

  public String getTags() {
    return tags;
  }
}