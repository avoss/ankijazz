package de.jlab.scales.anki;

import de.jlab.scales.Utils;

public class SimpleCard implements Card {
  private final int difficulty;
  private final String[] fields;

  public SimpleCard(int difficulty, String... fields) {
    this.difficulty = difficulty;
    this.fields = fields;
  }

  @Override
  public int getDifficulty() {
    return difficulty;
  }

  @Override
  public String getCsv() {
    return Utils.toCsv(fields);
  }
  @Override
  public String getHtml() {
    throw new UnsupportedOperationException();
  }

}
