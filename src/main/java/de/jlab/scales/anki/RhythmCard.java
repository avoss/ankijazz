package de.jlab.scales.anki;

import static java.lang.String.format;

import java.util.LinkedHashSet;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.AbstractRhythm;

public class RhythmCard extends LilyCard {

  private AbstractRhythm rhythm;

  public RhythmCard(AbstractRhythm rhythm) {
    super(new LilyRhythm(rhythm).toLily());
    this.rhythm = rhythm;
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
  public int getDifficulty() {
    return rhythm.getDifficulty();
  }

  public String getMetronomeMp3Name() {
    return "AnkiJazz-Metronome70.mp3";
  }
  
  public String getTitle() {
    return rhythm.getTitle();
  }
  

}
