package de.jlab.scales.anki;

public class BasicCard implements Card {
  private final int difficulty;
  private final String[] fields;
 
  public BasicCard(int difficulty, String... fields) {
    this.difficulty = difficulty;
    this.fields = fields;
  }

  @Override
  public int getDifficulty() {
    return difficulty;
  }

  @Override
  public String[] getFields() {
    return fields;
  }

  @Override
  public void setId(String id) {
     // ignore    
  }

}
