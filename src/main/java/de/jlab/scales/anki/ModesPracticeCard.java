package de.jlab.scales.anki;

import static java.lang.String.format;

import de.jlab.scales.Utils;

public class ModesPracticeCard extends LilyCard {

  private int difficulty;
  private ScaleModel model;

  public ModesPracticeCard(ScaleModel model) {
    super(model.getLilyScale().toLily());
    this.model = model;
    this.difficulty = model.getDifficulty();
  }
  
  public String getTags() {
    return Utils.tags(
        format("KeyOf %s", model.getModeRootName()), 
        format("Mode %s", model.getModeTypeName()), 
        format("Direction %s", model.getDirection()),
        format("Clef %s", model.getClef()),
        format("Instrument %s", model.getInstrument())
     );
  }
  
  @Override
  public int getDifficulty() {
    return difficulty;
  }
  
  public ScaleModel getModel() {
    return model;
  }
}
