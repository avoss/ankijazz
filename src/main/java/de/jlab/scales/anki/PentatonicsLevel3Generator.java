package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.LEFT;
import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.RIGHT;
import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5flat9;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sus4;
import static de.jlab.scales.theory.BuiltinChordType.Major6;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Minor6;
import static de.jlab.scales.theory.BuiltinChordType.Minor7;
import static de.jlab.scales.theory.BuiltinChordType.Minor7b5;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;
import static de.jlab.scales.theory.ScaleUniverse.PENTAS;
import static de.jlab.scales.theory.ScaleUniverse.SCALES;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.fretboard2.Fingering;
import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.GuitarString;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.fretboard2.MidiFretboardRenderer;
import de.jlab.scales.fretboard2.NPS;
import de.jlab.scales.fretboard2.PngFretboardRenderer;
import de.jlab.scales.fretboard2.Position;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.PentatonicChooser;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

public class PentatonicsLevel3Generator extends AbstractFretboardGenerator {

  
  public PentatonicsLevel3Generator(LoopIteratorFactory iteratorFactory) {
    super(iteratorFactory, " Pentatonics Level 3: Visualize Chords (Fretboard Diagrams)", "PentatonicsLevel3VisualizeChords");
  }
  
  @Override
  protected Collection<ChordScalePair> findPairs() {
    List<ChordScalePair> pairs = new ArrayList<>();
    PentatonicChooser chooser = new PentatonicChooser();
    for (BuiltinChordType type : List.of(Minor7, Major7, Major7Sharp11, Major6, Dominant7sus4, Minor6, Dominant7, Dominant7sharp5flat9, Minor7b5)) {
      Scale penta = chooser.chooseBest(type.getPrototype());
      pairs.add(new ChordScalePair(type, penta));
    }
    return pairs;
  }
  

}
