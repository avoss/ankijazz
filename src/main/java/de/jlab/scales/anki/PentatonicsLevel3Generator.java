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

public class PentatonicsLevel3Generator extends AbstractCardGenerator<FretboardDiagramCard> {

  @lombok.Data
  @lombok.RequiredArgsConstructor
  static class ChordPentaPair {
    private final ScaleType chord;
    private final Scale penta;
  }
  
  public PentatonicsLevel3Generator(LoopIteratorFactory iteratorFactory) {
    super(iteratorFactory, " Pentatonics Level 3: Visualize Chords (Fretboard Diagrams)", "PentatonicsLevel3VisualizeChords");
  }
  
  Collection<ChordPentaPair> findPairs() {
    List<ChordPentaPair> pairs = new ArrayList<>();
    PentatonicChooser chooser = new PentatonicChooser();
    for (BuiltinChordType type : List.of(Minor7, Major7, Major7Sharp11, Major6, Dominant7sus4, Minor6, Dominant7, Dominant7sharp5flat9, Minor7b5)) {
      Scale penta = chooser.chooseBest(type.getPrototype());
      pairs.add(new ChordPentaPair(type, penta));
    }
    return pairs;
  }
  
  @Override
  public Collection<? extends FretboardDiagramCard> generate() {
    List<FretboardDiagramCard> result = new ArrayList<>();
    Iterator<Note> roots = loopIterator(Scales.CMajor.asList());
    for (ChordPentaPair pair : findPairs()) {
      for (int string = 0; string < STANDARD_TUNING.getStrings().size(); string ++) {
        Note root = roots.next();
        FretboardDiagramCard card1 = createCard(pair, root, LEFT, string);
        FretboardDiagramCard card2 = createCard(pair, root, RIGHT, string);
        result.add(card1);
        if (!card1.getAssetId().equals(card2.getAssetId())) {
          result.add(createCard(pair, roots.next(), RIGHT, string));
        }
      }
    }
    return result;
  }

  private FretboardDiagramCard createCard(ChordPentaPair pair, Note root, BoxPosition box, int string) {
    Scale chord = pair.getChord().getPrototype().transpose(root.ordinal());
    ScaleInfo chordInfo = CHORDS.findFirstOrElseThrow(chord);
    Scale penta = pair.getPenta().transpose(root.ordinal());
    ScaleInfo pentaInfo = PENTAS.findFirstOrElseThrow(penta);
    
    Fingering fingering = NPS.caged(pentaInfo.getScaleType()).transpose(penta.getRoot());
    Fretboard frontBoard = new Fretboard();
    Position position = Marker.box(frontBoard, string, chord.getRoot(), box, fingering, Marker.BACKGROUND);
    Supplier<BufferedImage> frontImage = () -> new PngFretboardRenderer(frontBoard, false).render();

    int rootFret = findFirstMarkedFret(frontBoard, string);

    Fretboard backBoard = new Fretboard();
    backBoard.mark(position, Marker.outline(penta));
    backBoard.mark(string, rootFret, Marker.BACKGROUND);
    Supplier<BufferedImage> backImage = () -> new PngFretboardRenderer(backBoard, false).render();
    Supplier<Part> backMidi = () -> {
      if (penta.contains(chord.getRoot())) {
        return MidiFretboardRenderer.builder()
            .fretboard(backBoard)
            .backgroundChord(chord)
            .renderBackground(true)
            .build()
            .render();
      }
      return MidiFretboardRenderer.builder()
        .fretboard(backBoard)
        .backgroundChord(chord)
        .renderForeground(true)
        .foregroundIncludesRoot(true)
        .build()
        .render();
    };

    String title = String.format("Outline %s Chord", chordInfo.getScaleName());
    return FretboardDiagramCard.builder()
        .title(title)
        .fretNumber(backBoard.getMinFret())
        .chordInfo(chordInfo)
        .scaleInfo(pentaInfo)
        .frontImage(frontImage)
        .backImage(backImage)
        .backMidi(backMidi)
        .build();
  }

  private int findFirstMarkedFret(Fretboard fretboard, int stringIndex) {
    GuitarString string = fretboard.getString(stringIndex);
    for (int i = 0; i < 24; i++) {
      if (string.markerOf(i) != Marker.EMPTY) {
        return i;
      }
    }
    throw new IllegalStateException("Marker not found on string:" + string);
  }

}
