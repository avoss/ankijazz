package de.jlab.scales.anki;

import java.util.function.Function;

import de.jlab.scales.fretboard.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Caged3ModesFretboards extends AbstractCagedGenerator {

  public Caged3ModesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - CAGED 3: Modes (Fretboard)", "Caged3ModesFretboards");
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
