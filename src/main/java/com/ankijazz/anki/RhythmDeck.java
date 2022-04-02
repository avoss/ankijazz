package com.ankijazz.anki;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.List;

import com.ankijazz.Utils;
import com.ankijazz.Utils.Interpolator;
import com.ankijazz.lily.LilyMetronome;
import com.ankijazz.lily.LilyRhythm;
import com.ankijazz.lily.LilyMetronome.Tempo;
import com.ankijazz.rhythm.AbstractRhythm;
import com.ankijazz.rhythm.RhythmGenerator;

public class RhythmDeck extends AbstractDeck<RhythmCard> {

  private final LilyMetronome metronome;
  
  public RhythmDeck(LilyRhythm.Type type, int minBpm, int maxBpm) {
    super(format("AnkiJazz - Rhythm Notation (%d bpm)", minBpm), format("Rhythm%s%dDeck", type.getLabel(), minBpm));
    this.metronome = new LilyMetronome(5, minBpm, maxBpm);
    RhythmGenerator generator = new RhythmGenerator(Utils.randomLoopIteratorFactory());
    List<AbstractRhythm> rhythms = generator.generate();
    Interpolator tempoInterpolator = Utils.interpolator(0, rhythms.size(), minBpm, maxBpm);
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
