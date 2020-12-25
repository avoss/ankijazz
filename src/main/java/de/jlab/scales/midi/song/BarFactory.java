package de.jlab.scales.midi.song;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class BarFactory {

  private List<Scale[]> candidates = new ArrayList<>();
  private Iterator<Scale[]> iterator;

  public BarFactory add(int probability, Scale ...scales) {
    IntStream.range(0, probability).forEach(i -> candidates.add(scales));
    iterator = Utils.randomLoopIterator(candidates);
    return this;
  }

  public Bar next(KeySignature keySignature) {
    int semitones = keySignature.getNotationKey().ordinal();
    List<Chord> chords = new ArrayList<>();
    Scale scales[] = iterator.next();
    for (Scale scale : scales) {
      scale = scale.transpose(semitones);
      ScaleInfo scaleInfo = ScaleUniverse.CHORDS.findFirstOrElseThrow(scale);
      String symbol = String.format("%s%s", keySignature.notate(scale.getRoot()), scaleInfo.getTypeName());
      chords.add(new Chord(scale, symbol));
    }
    return new Bar(chords);
  }

}
