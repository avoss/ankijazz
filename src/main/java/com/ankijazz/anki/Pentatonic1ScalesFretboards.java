package com.ankijazz.anki;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import com.ankijazz.Utils;
import com.ankijazz.fretboard.Marker;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

public class Pentatonic1ScalesFretboards extends AbstractFretboardGenerator {

  
  public Pentatonic1ScalesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - Pentatonics 1: Scales (Fretboard)", "Pentatonic1ScalesFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.pentatonicScales();
  }
  
  @Override
  protected String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
    return scaleInfo.getScaleName();
  }

  @Override
  protected Note getFrontRoot(Scale chord, Scale scale) {
    return scale.getRoot();
  }
  
  @Override
  protected Function<Note, Marker> getOutlineMarker(Scale chord, Scale scale) {
    return Marker.outline(scale);
  }
  
  @Override
  protected boolean foregroundIncludesRoot(Scale chord, Scale scale) {
    return true;
  }
  
  @Override
  protected boolean playScaleThenChord() {
    return false;
  }
  
  protected Iterator<Note> getNoteIterator() {
    return Utils.randomLoopIterator(Arrays.asList(Note.values()));
  }
  
}
