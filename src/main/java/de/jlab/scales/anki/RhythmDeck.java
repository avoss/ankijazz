package de.jlab.scales.anki;

import static java.lang.String.format;

import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck {
  enum Tempo {
    SLOW(55), MEDIUM(70);

    private int bpm;

    Tempo(int bpm) {
      this.bpm = bpm;
    }
    public int getBpm() {
      return bpm;
    }
  }
  public RhythmDeck(Tempo tempo) {
    super(format("AnkiJazz-RhythmDeck%dbpm", tempo.getBpm()), format("AnkiJazz - Read and Play Rhythms (%d bpm)", tempo.getBpm()));
    RhythmGenerator generator = new RhythmGenerator();
    for (AbstractRhythm rhythm : generator.generate()) {
      add(new RhythmCard(rhythm, tempo.getBpm()));
    }
  }

}
