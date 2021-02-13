package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Fretboard.ROOTS_ONLY;
import static de.jlab.scales.theory.ScaleUniverse.PENTAS;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Fretboard.MarkedFret;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class Pentatonic5ModesFretboards extends AbstractFretboardGenerator {

  
  public Pentatonic5ModesFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - Pentatonics 5: Outline Modes (Fretboard)", "Pentatonic5ModesFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.pentatonicModes();
  }
  
  @Override
  protected String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
    return scaleInfo.getScaleName();
  }

  protected Note getFrontRoot(Scale chord, Scale scale) {
    return scale.getRoot();
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
  protected ScaleInfo findChordInfo(Scale chord) {
    return PENTAS.findFirstOrElseThrow(chord);
  }
  
  @Override
  protected boolean playScaleThenChord() {
    return true;
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
