package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Fretboard.ROOTS_ONLY;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;

import java.util.Collection;
import java.util.List;

import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Fretboard.MarkedFret;
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
  
  // TODO copy/paste
  private int cheatCount = 0;
  @Override
  protected void applyParticularities(Fretboard frontBoard, Fretboard backBoard, Scale chord, Scale scale) {
    List<MarkedFret> front = frontBoard.findMarkedFrets(ROOTS_ONLY);
    List<MarkedFret> back = backBoard.findMarkedFrets(ROOTS_ONLY);
    if (!back.containsAll(front)) {
      frontBoard.clear();
      frontBoard.mark(back.get(0));
      if (cheatCount++ > 3) {
        throw new IllegalStateException("more than 3 positions were not found in CAGED system ... please check");
      }
    }
  }

}
