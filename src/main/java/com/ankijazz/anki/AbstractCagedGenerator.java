package com.ankijazz.anki;

import static com.ankijazz.theory.ScaleUniverse.CHORDS;

import java.util.Collection;

import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

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
