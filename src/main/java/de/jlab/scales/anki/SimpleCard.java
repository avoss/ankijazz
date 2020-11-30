package de.jlab.scales.anki;

import java.nio.file.Path;

public class SimpleCard extends MustacheCard {
  private final double difficulty;
  private final String front;
  private final String back;
  private final String tags;

  public SimpleCard(double difficulty, String front, String back, String tags) {
    this.difficulty = difficulty;
    this.front = front;
    this.back = back;
    this.tags = tags;
  }

  @Override
  public double getDifficulty() {
    return difficulty;
  }

  @Override
  public String getCsv() {
    return String.format("%s;%s;%s", front, back, tags);
  }
  
  public String getFront() {
    return front;
  }
  
  public String getBack() {
    return back;
  }
  
  public String getTags() {
    return tags;
  }

  @Override
  public void writeAssets(Path directory) {
  }

}
