package de.jlab.scales.anki;

import static de.jlab.scales.theory.ScaleUniverse.CHORDS;

import java.util.Collection;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public abstract class AbstractCagedGenerator extends AbstractFretboardGenerator {

  protected AbstractCagedGenerator(Validator validator, String title, String fileName) {
    super(validator, title, fileName);
  }

  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.cagedModes();
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
  protected ScaleInfo findChordInfo(Scale chord) {
    return CHORDS.findFirstOrElseThrow(chord);
  }
  
}
