package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.LEFT;
import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.RIGHT;
import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;
import static de.jlab.scales.theory.ScaleUniverse.SCALES;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.anki.AbstractFretboardGenerator.ScaleChordPair;
import de.jlab.scales.fretboard2.Fingering;
import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.GuitarString;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.fretboard2.MidiFretboardRenderer;
import de.jlab.scales.fretboard2.NPS;
import de.jlab.scales.fretboard2.PngFretboardRenderer;
import de.jlab.scales.fretboard2.Position;
import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

public abstract class AbstractFretboardGenerator implements CardGenerator<FretboardDiagramCard> {

  @lombok.RequiredArgsConstructor
  @lombok.ToString
  @lombok.EqualsAndHashCode
  protected static class ScaleChordPair {
    private final Scale chord;
    private final Scale scale;
    public Scale getChord() {
      return chord;
    }
    public Scale getScale() {
      return scale;
    }
  }
  
  private final String title;
  private final String fileName;
  private LoopIteratorFactory iteratorFactory;

  protected AbstractFretboardGenerator(LoopIteratorFactory iteratorFactory, String title, String fileName) {
    this.iteratorFactory = iteratorFactory;
    this.title = title;
    this.fileName = fileName;
  }
  
  protected abstract Collection<ScaleChordPair> findPairs();
  protected abstract String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo);
  protected abstract Function<Note, Marker> getOutlineMarker(Scale scale, Scale chord);
  
  @Override
  public Collection<? extends FretboardDiagramCard> generate() {
    List<FretboardDiagramCard> result = new ArrayList<>();
    Iterator<Note> roots = loopIterator(Scales.CMajor.asList());
    for (ScaleChordPair pair : findPairs()) {
      for (int string = 0; string < STANDARD_TUNING.getStrings().size(); string ++) {
        for (BoxPosition box : BoxPosition.values()) {
          result.add(createCard(pair, roots.next(), box, string));
        }
      }
    }
    return result;
  }

  private FretboardDiagramCard createCard(ScaleChordPair pair, Note root, BoxPosition box, int stringNumber) {
    Scale chord = pair.getChord().transpose(root.ordinal());
    ScaleInfo chordInfo = CHORDS.findFirstOrElseThrow(chord);
    Scale scale = pair.getScale().transpose(root.ordinal());
    ScaleInfo scaleInfo = SCALES.findFirstOrElseThrow(scale);
    
    Fingering fingering = NPS.caged(scaleInfo.getScaleType()).transpose(scale.getRoot());
    Fretboard frontBoard = new Fretboard();
    Position position = Marker.box(frontBoard, stringNumber, chord.getRoot(), box, fingering, Marker.BACKGROUND);
    Supplier<BufferedImage> frontImage = () -> new PngFretboardRenderer(frontBoard, false).render();

    int rootFret = findFirstMarkedFret(frontBoard, stringNumber);

    Fretboard backBoard = new Fretboard();
    backBoard.mark(position, getOutlineMarker(scale, chord));
    backBoard.mark(stringNumber, rootFret, Marker.BACKGROUND);
    Supplier<BufferedImage> backImage = () -> new PngFretboardRenderer(backBoard, true).render();
    Supplier<Part> backMidi = () -> {
      if (scale.contains(chord.getRoot())) {
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

    String cardTitle = getCardTitle(chordInfo, scaleInfo);
    return FretboardDiagramCard.builder()
        .title(cardTitle)
        .fretNumber(backBoard.getMinFret())
        .stringNumber(stringNumber)
        .chordInfo(chordInfo)
        .scaleInfo(scaleInfo)
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
  

  @Override
  public String getFileName() {
    return fileName;
  }
  
  @Override
  public String getTitle() {
    return title;
  }
  
  protected <U> Iterator<U> loopIterator(Collection<? extends U> collection) {
    return iteratorFactory.iterator(collection);
  }
  
}
