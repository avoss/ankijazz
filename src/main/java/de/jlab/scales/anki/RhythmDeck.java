package de.jlab.scales.anki;

import de.jlab.scales.rhythm.Rhythm;
import de.jlab.scales.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck {

  public RhythmDeck() {
    super("AnkiJazz - Read and Play Rhythms");
    RhythmGenerator generator = new RhythmGenerator();
    for (Rhythm rhythm : generator.generate()) {
      add(new RhythmCard(new RhythmModel(rhythm)));
    }
  }

}
