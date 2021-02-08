package de.jlab.scales.anki;

import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class CagedLevel3Generator extends AbstractCagedGenerator {

  public CagedLevel3Generator(Validator validator) {
    super(validator, "CAGED Level 3: Mode Positions (Fretboard Diagrams)", "CAGEDLevel3ModePositions");
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
