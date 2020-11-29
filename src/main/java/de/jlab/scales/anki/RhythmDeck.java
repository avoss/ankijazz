package de.jlab.scales.anki;

import static java.lang.String.format;

import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck {
  
  
  public RhythmDeck(LilyRhythm.Tempo tempo, LilyRhythm.Type type) {
    super(format("AnkiJazz-RhythmDeck%s%dbpm", type.getLabel(), tempo.getBpm()), format("AnkiJazz - Read and Play Rhythms (%d bpm)", tempo.getBpm()));
    RhythmGenerator generator = new RhythmGenerator();
    for (AbstractRhythm rhythm : generator.generate()) {
      add(new RhythmCard(rhythm, tempo, type));
    }
  }

}
