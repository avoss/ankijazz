package de.jlab.scales.anki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class CagedLevel5Generator extends AbstractCagedGenerator {

  public CagedLevel5Generator(Validator validator) {
    super(validator, "CAGED Level 5: Learn Modes (Fretboard Diagrams)", "CAGEDLevel5LearnModes");
  }

  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    List<ChordScaleAudio> pairs = new ArrayList<>();
    for (Scale scale : Scales.commonModes(false)) {
      Scale chord = findArpeggio(scale);
      Scale audio = scale.getChord(0);
      pairs.add(new ChordScaleAudio(chord, scale, audio));
    }
    return pairs;
  }
  
  private Scale findArpeggio(Scale scale) {
    if (scale.isAlteredDominant()) {
      return Scales.C7sharp5.transpose(scale.getRoot());
    }
    return scale.getChord(0);
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
