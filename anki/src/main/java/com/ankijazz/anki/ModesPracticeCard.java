package com.ankijazz.anki;

import static com.ankijazz.anki.AnkiUtils.ankiMp3;
import static com.ankijazz.anki.AnkiUtils.ankiPng;

import java.util.LinkedHashMap;
import java.util.Map;
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
        model.getModeName(),
        model.getModeTypeName(),
        model.getModeRootName(),
        model.getParentName(),
        model.getParentTypeName(),
        model.getParentRootName(),
        ankiMp3(super.getMp3Name()),
        ankiPng(super.getPngName()),
        model.getDirection()).collect(Collectors.joining(CSV_DELIMITER));
  }

  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("modeName", model.getModeName());
    map.put("modeType", model.getModeTypeName());
    map.put("modeRoot", model.getModeRootName());
    map.put("parentName", model.getParentName());
    map.put("parentType", model.getParentTypeName());
    map.put("parentRoot", model.getParentRootName());
    map.put("modeMp3", getMp3Name());
    map.put("modePng", getPngName());
    map.put("direction", model.getDirection());
    map.put("difficulty", model.getDifficulty());
    return map;
  }
}
