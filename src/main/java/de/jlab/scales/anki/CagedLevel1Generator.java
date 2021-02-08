package de.jlab.scales.anki;

import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class CagedLevel1Generator extends AbstractCagedGenerator {

  protected CagedLevel1Generator(Validator validator) {
    super(validator, "CAGED Level 1: Learn Scale Positions (Fretboard Diagrams)", "CAGEDLevel1LearnScalePositions");
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
