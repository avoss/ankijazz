package de.jlab.scales.anki;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModesPracticeCard extends LilyCard {

  private double difficulty;
  private ScaleModel model;

  public ModesPracticeCard(ScaleModel model) {
    super(model.getLilyScale().toLily());
    this.model = model;
    this.difficulty = model.getDifficulty();
  }
  
  @Override
  public double getDifficulty() {
    return difficulty;
  }
  
  public ScaleModel getModel() {
    return model;
  }

  @Override
  public String getCsv() {
    return Stream.of(
        model.getParentName(),
        model.getParentTypeName(),
        model.getParentRootName(),
        model.getModeName(),
        model.getModeTypeName(),
        model.getModeRootName(),
        super.getMp3Name(),
        super.getPngName(),
        model.getDirection()).collect(Collectors.joining(";"));
  }
}
