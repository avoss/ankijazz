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

public class ChordFactory {

  private List<Scale> candidates = new ArrayList<>();
  private Iterator<Scale> iterator;

  public ChordFactory add(Scale scale, int probability) {
    IntStream.range(0, probability).forEach(i -> candidates.add(scale));
    iterator = Utils.randomLoopIterator(candidates);
    return this;
  }

  // TODO semitones == keySignature.getNotationKey().ordinal() ???!!!!
  public Chord next(int transposeSemitones, KeySignature keySignature) {
    Scale scale = iterator.next().transpose(transposeSemitones);
    ScaleInfo scaleInfo = ScaleUniverse.CHORDS.findFirstOrElseThrow(scale);
    String symbol = String.format("%s%s", keySignature.notate(scale.getRoot()), scaleInfo.getTypeName());
    return new Chord(scale, symbol);
  }

}
