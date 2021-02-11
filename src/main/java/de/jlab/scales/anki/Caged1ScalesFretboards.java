package de.jlab.scales.anki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class Caged1ScalesFretboards extends AbstractCagedGenerator {

  public Caged1ScalesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - CAGED 1: Scale Positions (Fretboard Diagrams)", "Caged1ScalesFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    List<ChordScaleAudio> pairs = new ArrayList<>();
    for (Scale scale : Scales.commonScales(false)) {
      Scale chord = scale.getChord(0);
      pairs.add(new ChordScaleAudio(chord, scale, chord));
    }
    return pairs;
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
