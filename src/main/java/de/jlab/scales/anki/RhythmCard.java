package de.jlab.scales.anki;

import static java.lang.String.format;

import java.util.LinkedHashSet;
import java.util.Set;

import de.jlab.scales.Utils;
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
  
  public String getTags() {
    Set<String> tags = new LinkedHashSet<>();
    if (rhythm.hasTies()) {
      tags.add("Rhythm Tied");
    }
    tags.add(format("Rhythm %d", rhythm.getUniqueQuarters().size()));
    tags.add(format("Rhythm %s", rhythm.getTypeName()));    
    return Utils.tags(tags);
  }

  @Override
  public double getDifficulty() {
    return difficulty;
  }
  
  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    model.doubleTerm(0, 1, 60).update(rhythm.getDifficulty());
    model.doubleTerm(tempo.getMinBpm(), tempo.getMaxBpm(), 40).update(tempo.getBpm());
    return model.getDifficulty();
  }

  public String getMetronomeMp3Name() {
    return tempo.getMp3Name();
  }
  
  public String getTitle() {
    return String.format("%s (%d bpm)", rhythm.getTitle(), tempo.getBpm());
  }
  

}
