package com.ankijazz.anki;

import static com.ankijazz.theory.ScaleUniverse.PENTAS;

import java.util.Collection;
import java.util.function.Function;

import com.ankijazz.fretboard.Marker;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

public class Pentatonic5ModesFretboards extends AbstractFretboardGenerator {

  
  public Pentatonic5ModesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - Pentatonics 5: Outline Modes (Fretboard)", "Pentatonic5ModesFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.pentatonicModes();
  }
  
  @Override
  protected String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
    return scaleInfo.getScaleName();
  }

  protected Note getFrontRoot(Scale chord, Scale scale) {
    return scale.getRoot();
  }

  @Override
  protected Function<Note, Marker> getOutlineMarker(Scale chord, Scale scale) {
    return Marker.outline(chord.superimpose(scale.getRoot()));
  }

  @Override
  protected boolean foregroundIncludesRoot(Scale chord, Scale scale) {
    return chord.contains(scale.getRoot());
  }
  
  @Override
  protected ScaleInfo findChordInfo(Scale chord) {
    return PENTAS.findFirstOrElseThrow(chord);
  }
  
  @Override
  protected boolean playScaleThenChord() {
    return true;
  }
  
}
