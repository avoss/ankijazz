package de.jlab.scales.anki;

import java.util.Collection;
import java.util.function.Function;

import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class Pentatonic3ChordsFretboards extends AbstractFretboardGenerator {

  
  public Pentatonic3ChordsFretboards(Validator validator) {
    super(validator, "AnkiJazz Guitar - Pentatonics 3: Outline Chords (Fretboard)", "Pentatonic3ChordsFretboards");
  }
  
  @Override
  protected Collection<ChordScaleAudio> findPairs() {
    return ChordScaleAudio.pentatonicChords();
  }
  
  @Override
  protected String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
    String chordRootName = scaleInfo.getKeySignature().notate(chordInfo.getScale().getRoot());    
    return String.format("Outline %s%s Chord", chordRootName, chordInfo.getTypeName());
  }

  @Override
  protected Note getFrontRoot(Scale chord, Scale scale) {
    return chord.getRoot();
  }
  
  @Override
  protected Function<Note, Marker> getOutlineMarker(Scale chord, Scale scale) {
    return Marker.outline(scale.superimpose(chord.getRoot()));
  }
  
  @Override
  protected boolean foregroundIncludesRoot(Scale chord, Scale scale) {
    return scale.contains(chord.getRoot());
  }
  
  @Override
  protected boolean playScaleThenChord() {
    return false;
  }

  @Override
  protected void applyParticularities(Fretboard frontBoard, Fretboard backBoard, Scale chord, Scale scale) {
    if (!scale.contains(chord.getRoot())) {
      backBoard.markVisible(chord.getRoot(), Marker.ROOT);
    }
  }
}
