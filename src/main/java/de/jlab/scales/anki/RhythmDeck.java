package de.jlab.scales.anki;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.lily.LilyMetronome;
import de.jlab.scales.lily.LilyMetronome.Tempo;
import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck<RhythmCard> {

  private static final int MIN_BPM = 55;
  private static final int MAX_BPM = 75;
  private LilyMetronome metronome = new LilyMetronome(5, MIN_BPM, MAX_BPM);
  
  public RhythmDeck(LilyRhythm.Type type) {
    super(format(String.format("AnkiJazz - Read and Play Rhythms (%d .. %d bpm)", MIN_BPM, MAX_BPM), "AnkiJazz-RhythmDeck%s", type.getLabel()));
    RhythmGenerator generator = new RhythmGenerator();
    List<AbstractRhythm> rhythms = generator.generate();
    Interpolator tempoInterpolator = Utils.interpolator(0, rhythms.size(), MIN_BPM, MAX_BPM);
    int index = 0;
    for (AbstractRhythm rhythm : rhythms) {
      Tempo tempo = metronome.tempo(tempoInterpolator.apply(index));
      add(new RhythmCard(rhythm, tempo, type));
      index ++;
    }
  }
  
  @Override
  public void writeAssets(Path dir) {
    super.writeAssets(dir);
    metronome.writeAssets(dir);
  }

}
