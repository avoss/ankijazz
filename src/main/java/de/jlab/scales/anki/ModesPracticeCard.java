package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;

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
        ankiMp3(super.getMp3Name()),
        ankiPng(super.getPngName()),
        model.getDirection()).collect(Collectors.joining(";"));
  }
}
