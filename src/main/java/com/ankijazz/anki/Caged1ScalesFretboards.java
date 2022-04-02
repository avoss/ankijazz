package de.jlab.scales.anki;

import java.util.Collection;
import java.util.function.Function;

import de.jlab.scales.fretboard.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Caged1ScalesFretboards extends AbstractCagedGenerator {

  public Caged1ScalesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - CAGED 1: Scales (Fretboard)", "Caged1ScalesFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.cagedScales();
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

}
