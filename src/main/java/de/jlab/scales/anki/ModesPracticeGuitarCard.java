package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;
import static java.lang.String.format;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.jlab.scales.Utils;

public class ModesPracticeGuitarCard extends LilyCard {

  private FretboardPosition position;
  private double difficulty;
  private ScaleModel model;

  public ModesPracticeGuitarCard(ScaleModel model, FretboardPosition position) {
    super(model.getLilyScale().toLily());
    this.model = model;
    this.difficulty = model.getDifficulty();
    this.position = position;
  }
  
  public FretboardPosition getPosition() {
    return position;
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
        model.getDirection(),
        position.getLabel(),
        position.getImage()).collect(Collectors.joining(";"));
  }

}
