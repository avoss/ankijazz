package de.jlab.scales.anki;

import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class CagedLevel5Generator extends AbstractCagedGenerator {

  public CagedLevel5Generator(Validator validator) {
    super(validator, "CAGED Level 5: Mode Positions with Chords highlighted (Fretboard Diagrams)", "CAGEDLevel5ModesWithChords");
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
