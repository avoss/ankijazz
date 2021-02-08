package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Fretboard.ROOTS_ONLY;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Fretboard.MarkedFret;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.Scales;

public class CagedLevel3Generator extends AbstractFretboardGenerator {

  public CagedLevel3Generator(Validator validator) {
    super(validator, "CAGED Level 3: Visualize Modes and Arpeggios (Fretboard Diagrams)", "CAGEDLevel3VisualizeModes");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    List<ChordScaleAudio> pairs = new ArrayList<>();
    for (Scale scale : Scales.commonModes(false)) {
//    for (Scale scale : List.of(Scales.CMajor.superimpose(Note.F))) {
      Scale chord = scale.getChord(0);
      pairs.add(new ChordScaleAudio(chord, scale, chord));
    }
    return pairs;
  }
  
  @Override
  protected String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
    return String.format("Outline %s", scaleInfo.getScaleName());
  }

  @Override
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
    return CHORDS.findFirstOrElseThrow(chord);
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
