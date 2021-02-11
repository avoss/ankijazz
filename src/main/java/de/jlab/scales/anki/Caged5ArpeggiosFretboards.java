package de.jlab.scales.anki;

import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Caged5ArpeggiosFretboards extends AbstractCagedGenerator {

  public Caged5ArpeggiosFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - CAGED 5: Modes and Arpeggios (Fretboard)", "Caged5ArpeggiosFretboards");
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
  protected boolean playScaleThenChord() {
    return true;
  }

}
