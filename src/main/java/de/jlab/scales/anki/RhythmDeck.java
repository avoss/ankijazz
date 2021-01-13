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

  private static final int MIN_BPM = 60;
  private static final int MAX_BPM = 80;
  private LilyMetronome metronome;
  
  public RhythmDeck(LilyRhythm.Type type) {
    super(format(String.format("AnkiJazz - Read and Play Rhythms (%d .. %d bpm)", MIN_BPM, MAX_BPM)), "Rhythm".concat(type.getLabel()).concat("Deck"));
    this.metronome = new LilyMetronome(5, MIN_BPM, MAX_BPM);
    RhythmGenerator generator = new RhythmGenerator(Utils.randomLoopIteratorFactory());
    List<AbstractRhythm> rhythms = generator.generate();
    Interpolator tempoInterpolator = Utils.interpolator(0, rhythms.size(), MIN_BPM, MAX_BPM);
    int index = 0;
    for (AbstractRhythm rhythm : rhythms) {
      Tempo tempo = metronome.tempo(tempoInterpolator.apply(index));
      add(new RhythmCard(rhythm, tempo, type));
      index ++;
    }
  }
  
  public RhythmDeck(String title, String outputFileName, String mustacheTemplate, List<RhythmCard> cards, LilyMetronome metronome) {
    super(title, outputFileName, mustacheTemplate, cards);
    this.metronome = metronome;
  }

  @Override
  public void writeAssets(Path dir) {
    super.writeAssets(dir);
    metronome.writeAssets(dir);
  }

  @Override
  protected Deck<RhythmCard> subdeck(String title, String outputFileName, String mustacheTemplate, List<RhythmCard> cards) {
    return new RhythmDeck(title, outputFileName, mustacheTemplate, cards, metronome);
  }
}
