package de.jlab.scales.anki;

import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck {

  public RhythmDeck() {
    super("AnkiJazz - Read and Play Rhythms");
    RhythmGenerator generator = new RhythmGenerator();
    for (AbstractRhythm rhythm : generator.generate()) {
      add(new RhythmCard(new RhythmModel(rhythm)));
    }
  }

}
