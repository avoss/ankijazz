package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;
import static java.util.stream.Collectors.joining;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.lily.LilyMetronome.Tempo;
import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.AbstractRhythm;

public class RhythmCard extends LilyCard {
  
  private final AbstractRhythm rhythm;
  private final Tempo tempo;
  private double difficulty;

  public RhythmCard(AbstractRhythm rhythm, Tempo tempo, LilyRhythm.Type type) {
    super(new LilyRhythm(rhythm, tempo.getBpm(), type).toLily());
    this.rhythm = rhythm;
    this.tempo = tempo;
    this.difficulty = computeDifficulty();
  }
  
  @Override
  public double getDifficulty() {
    return difficulty;
  }
  
  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(0, 1, 60).update(rhythm.getDifficulty());
    model.doubleTerm(tempo.getMinBpm(), tempo.getMaxBpm(), 40).update(getBpm());
    return model.getDifficulty();
  }

  public String getMetronomeMp3Name() {
    return tempo.getMp3Name();
  }
  
  public String getTitle() {
    return String.format("%s (%d bpm)", rhythm.getTitle(), getBpm());
  }

  public String hasSyncopation() {
    return rhythm.hasSyncopation() ? "Yes" : "No";
  }

  public String hasTies() {
    return rhythm.hasTies() ? "Yes" : "No";
  }

  public int getNumberOfUniqueQuarters() {
    return rhythm.getUniqueQuarters().size();
  }
 
  public int getBpm() {
    return tempo.getBpm();
  }

  public String getTypeName() {
    return rhythm.getTypeName();
  }

  @Override
  public String getCsv() {
    return Stream.of(
        getTitle(),
        getTypeName(),
        ankiPng(getPngName()),
        ankiMp3(getMp3Name()),
        ankiMp3(getMetronomeMp3Name()),
        hasTies(),
        hasSyncopation(),
        Integer.toString(getNumberOfUniqueQuarters()),
        Integer.toString(getBpm())).collect(joining(CSV_DELIMITER));
  }
  
  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", getTitle());
    map.put("type", getTypeName());
    map.put("rhythmPng", getPngName());
    map.put("rhythmMp3", getMp3Name());
    map.put("metronomeMp3", getMetronomeMp3Name());
    map.put("hasTies", hasTies());
    map.put("hasSyncopation", hasSyncopation());
    map.put("numberOfUniqueQuarters", getNumberOfUniqueQuarters());
    map.put("bpm", getBpm());
    map.put("difficulty", getDifficulty());
    return map;
  }
}
