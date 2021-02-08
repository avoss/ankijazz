package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Fretboard.ROOTS_ONLY;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.ScaleUniverse.PENTAS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.fretboard2.Fretboard.MarkedFret;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.PentatonicChooser;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

public class PentatonicsLevel5Generator extends AbstractFretboardGenerator {

  
  public PentatonicsLevel5Generator(Validator validator) {
    super(validator, "Pentatonics Level 5: Outline Modes (Fretboard Diagrams)", "PentatonicsLevel5OutlineModes");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    List<ChordScaleAudio> pairs = new ArrayList<>();
    PentatonicChooser chooser = new PentatonicChooser();
    for (Scale scale : Scales.commonModes(false)) {
      if (!scaleContainsPentatonic(scale)) {
        continue;
      }
      Scale chord = scale.getChord(0);
      Scale penta = chooser.chooseBest(chord);
      pairs.add(new ChordScaleAudio(penta, scale, chord));
    }
    return pairs;
  }

  private boolean scaleContainsPentatonic(Scale scale) {
    ScaleType type = MODES.findFirstOrElseThrow(scale).getScaleType();
    return Set.of(BuiltinScaleType.Major, BuiltinScaleType.MelodicMinor).contains(type);
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
