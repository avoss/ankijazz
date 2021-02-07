package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;
import static de.jlab.scales.theory.ScaleUniverse.MODES;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import de.jlab.scales.Utils;
import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.fretboard2.Fingering;
import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.fretboard2.MidiFretboardRenderer;
import de.jlab.scales.fretboard2.NPS;
import de.jlab.scales.fretboard2.PngFretboardRenderer;
import de.jlab.scales.fretboard2.Position;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.Scales;

public abstract class AbstractFretboardGenerator implements CardGenerator<FretboardDiagramCard> {

  @lombok.Data
  protected static class ChordScaleAudio {
    private final Scale chord;
    private final Scale scale;
    private final Scale audio;
  }
  
  private final String title;
  private final String fileName;

  protected AbstractFretboardGenerator(String title, String fileName) {
    this.title = title;
    this.fileName = fileName;
  }
  
  protected abstract Collection<ChordScaleAudio> findPairs();
  protected abstract String getCardTitle(ScaleInfo chordInfo, ScaleInfo scaleInfo);
  protected abstract Function<Note, Marker> getOutlineMarker(Scale chord, Scale scale);
  protected abstract boolean foregroundIncludesRoot(Scale chord, Scale scale);
  protected abstract boolean playScaleThenChord();
  
  @Override
  public Collection<? extends FretboardDiagramCard> generate() {
    List<FretboardDiagramCard> result = new ArrayList<>();
    Iterator<Note> roots = Utils.loopIterator(Scales.CMajor.asList());
    for (ChordScaleAudio pair : findPairs()) {
      for (int string = 0; string < STANDARD_TUNING.getStrings().size(); string ++) {
        for (BoxPosition box : BoxPosition.values()) {
          result.add(createCard(pair, roots.next(), box, string));
        }
      }
    }
    return result;
  }

  private FretboardDiagramCard createCard(ChordScaleAudio pair, Note root, BoxPosition box, int stringNumber) {
    Scale chord = pair.getChord().transpose(root.ordinal());
    ScaleInfo chordInfo = findChordInfo(chord);
    Scale scale = pair.getScale().transpose(root.ordinal());
    ScaleInfo scaleInfo = MODES.findFirstOrElseThrow(scale);
    Scale audio = pair.getAudio().transpose(root.ordinal());
    Fingering fingering = NPS.caged(scaleInfo.getScaleType()).transpose(scaleInfo.getParentInfo().getScale().getRoot());
    
    Fretboard frontBoard = new Fretboard();
    Position position = Marker.box(frontBoard, stringNumber, chord.getRoot(), box, fingering, Marker.ROOT);
    Supplier<BufferedImage> frontImage = () -> new PngFretboardRenderer(frontBoard, false).render();

    Fretboard backBoard = new Fretboard();
    backBoard.mark(position, getOutlineMarker(chord, scale));
    if (!scale.contains(chord.getRoot())) {
      backBoard.markVisible(chord.getRoot(), Marker.ROOT);
    }
    Supplier<BufferedImage> backImage = () -> new PngFretboardRenderer(backBoard, true).render();
    Supplier<Part> backMidi = () -> {
      if (playScaleThenChord()) {
        return MidiFretboardRenderer.builder()
            .fretboard(backBoard)
            .backgroundChord(audio)
            .renderBackground(true)
            .renderForeground(true)
            .foregroundIncludesRoot(foregroundIncludesRoot(chord, scale))
            .build()
            .render();
      }
      return MidiFretboardRenderer.builder()
        .fretboard(backBoard)
        .backgroundChord(audio)
        .renderForeground(true)
        .foregroundIncludesRoot(foregroundIncludesRoot(chord, scale))
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

  protected ScaleInfo findChordInfo(Scale chord) {
    return CHORDS.findFirstOrElseThrow(chord);
  }

  @Override
  public String getFileName() {
    return fileName;
  }
  
  @Override
  public String getTitle() {
    return title;
  }
  
}
