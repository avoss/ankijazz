package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.*;
import static de.jlab.scales.theory.BuiltinScaleType.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.jlab.scales.Utils;
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
import de.jlab.scales.fretboard2.Tunings;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.ChordParser;
import de.jlab.scales.theory.ChordSubstitutionChooser.SubstitutionInfo;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.PentatonicChooser;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.ScaleUniverse;

public class OutlineChordsWithPentatonicsCardGenerator extends AbstractCardGenerator<FretboardDiagramCard> {
  /**
   * TODO show fret number on answer page
   */
  final int numberOfCards = 50;
  
  private static final ScaleUniverse PENTAS = new ScaleUniverse(false, List.of(Minor7Pentatonic, Minor6Pentatonic));

  @lombok.Data
  @lombok.RequiredArgsConstructor(staticName = "pair")
  static class ChordPentaPair {
    private final ScaleType chord;
    private final Scale penta;
  }
  
  public OutlineChordsWithPentatonicsCardGenerator(LoopIteratorFactory iteratorFactory) {
    super(iteratorFactory, "Outline Chords with Pentatonics", "OutlineChordsWithPentatonics");
  }
  
  Collection<ChordPentaPair> findAllPairs() {
    List<ChordPentaPair> pairs = new ArrayList<>();
    PentatonicChooser chooser = new PentatonicChooser();
    for (BuiltinChordType type : BuiltinChordType.values()) {
      if (type.getPrototype().getNumberOfNotes() < 4) {
        continue;
      }
      Scale chord = type.getPrototype();
      Optional<SubstitutionInfo> best = chooser.chooseBestInfo(chord);
      if (best.isPresent()) {
        pairs.add(new ChordPentaPair(type, best.get().getSubstitution()));
      }
    }
    return pairs;
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
    Iterator<ChordPentaPair> pairs = loopIterator(findPairs());
    Iterator<Note> roots = loopIterator(Arrays.asList(Note.values()));
    Iterator<BoxPosition> boxes = loopIterator(Arrays.asList(BoxPosition.values()));
    Iterator<Integer> strings = loopIterator(IntStream.range(0, Tunings.STANDARD_TUNING.getStrings().size()).boxed().collect(Collectors.toList()));
    for (int i = 0; i < numberOfCards; i++) {
      result.add(createCard(pairs.next(), roots.next(), boxes.next(), strings.next()));
    }
    return result;
  }

  // TODO: if root is contained in penta, it should be red. If not, it should be gray.
  // TODO: mark root of pentatonic?
  private FretboardDiagramCard createCard(ChordPentaPair pair, Note root, BoxPosition box, int string) {
    Scale chord = pair.getChord().getPrototype().transpose(root.ordinal());
    Scale penta = pair.getPenta().transpose(root.ordinal());
    
    ScaleInfo pentaInfo = PENTAS.findFirstOrElseThrow(penta);
    Fingering fingering = NPS.caged(pentaInfo.getScaleType()).transpose(penta.getRoot());
    Fretboard frontBoard = new Fretboard();
    Position position = Marker.box(frontBoard, string, chord.getRoot(), box, fingering);
    Supplier<BufferedImage> frontImage = () -> new PngFretboardRenderer(frontBoard).render();
    int rootFret = findRootFret(frontBoard, string);

    Fretboard backBoard = new Fretboard();
    backBoard.mark(string, rootFret, Marker.BACKGROUND);
    backBoard.mark(position, Marker.outline(penta));
    Supplier<BufferedImage> backImage = () -> new PngFretboardRenderer(backBoard).render();
    Supplier<Part> backMidi = () -> MidiFretboardRenderer.builder()
        .fretboard(backBoard)
        .backgroundChord(chord)
        .renderForeground(true)
        .foregroundIncludesRoot(true)
        .build()
        .render();
        
    String chordName = Utils.chordName(pair.getChord(), root);
    return new FretboardDiagramCard(chordName, frontImage, backImage, backMidi);
    

    
//    ScaleInfo scaleInfo = ScaleUniverse.SCALES.findScalesContaining(chord.asSet()).iterator().next();
//    
//    // FIXME: must transpose to mode root, e.g. to Bb for Cm7 dorian
//    Fingering fingering = NPS.caged(scaleInfo.getScaleType()).transpose(scaleInfo.getScale().getRoot());  
//    
//    Fretboard fretboard = new Fretboard();
//    Position scalePosition = Marker.box(fretboard, string, chord.getRoot(), box, fingering);
//    BufferedImage frontImage = new PngFretboardRenderer(fretboard).render();
//    
//    fretboard.mark(scalePosition, n -> Marker.EMPTY);
//    
//    // FIXME: must use NPS Pentatonic Fingering, not just visible (that marks too many frets)
//    fretboard.markVisible(penta, Marker.FOREGROUND);
//    BufferedImage backImage = new PngFretboardRenderer(fretboard).render();
//    Part backMidi = new MidiFretboardRenderer(fretboard, false, chord).render();
//    String chordName = Utils.chordName(pair.getChord(), root);
//    return new FretboardDiagramCard(chordName, frontImage, backImage, backMidi);
    
  }

  private int findRootFret(Fretboard fretboard, int stringIndex) {
    GuitarString string = fretboard.getString(stringIndex);
    for (int i = 0; i < 24; i++) {
      if (string.markerOf(i) == Marker.ROOT) {
        return i;
      }
    }
    throw new IllegalStateException("Root not found on string:" + string);
  }

}
