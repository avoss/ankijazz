package de.jlab.scales.anki;

import static de.jlab.scales.fretboard2.Tunings.STANDARD_TUNING;
import static de.jlab.scales.theory.ScaleUniverse.CHORDS;
import static de.jlab.scales.theory.ScaleUniverse.MODES;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import de.jlab.scales.Utils;
import de.jlab.scales.fretboard2.BoxMarker.BoxPosition;
import de.jlab.scales.fretboard2.Fretboard.Box;
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

  public interface Validator {
    void validate(Fretboard frontBoard, Fretboard backBoard);

    void validate(ScaleInfo chordInfo, ScaleInfo scaleInfo);

    void validate(Supplier<Part> backMidi);
  }

  @lombok.Data
  protected static class ChordScaleAudio {
    private final Scale chord;
    private final Scale scale;
    private final Scale audio;
  }

  @lombok.Data
  protected static class CardData {
    private final ChordScaleAudio chordScaleAudio;
    private final Note root;
    private final BoxPosition box;
    private final int stringIndex;
  }
  
  private final String title;
  private final String fileName;
  private Validator validator;

  protected AbstractFretboardGenerator(Validator validator, String title, String fileName) {
    this.validator = validator;
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
    for (CardData data: getCardData()) {
      result.add(createCard(data.getChordScaleAudio(), data.getRoot(), data.getBox(), data.getStringIndex()));
    }
    return result;
  }
  
  protected Collection<? extends CardData> getCardData() {
    List<CardData> result = new ArrayList<>();
    Iterator<Note> roots = Utils.loopIterator(Arrays.asList(Note.values()));
    for (ChordScaleAudio pair : findPairs()) {
      for (int string = 0; string < STANDARD_TUNING.getStrings().size(); string++) {
        for (BoxPosition box : BoxPosition.values()) {
          result.add(new CardData(pair, roots.next(), box, string));
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
    validator.validate(chordInfo, scaleInfo);
    Scale audio = pair.getAudio().transpose(root.ordinal());
    Fingering fingering = NPS.caged(scaleInfo.getScaleType()).transpose(scaleInfo.getParentInfo().getScale().getRoot());

    Fretboard frontBoard = new Fretboard();
    Position position = Marker.box(frontBoard, stringNumber, getFrontRoot(chord, scale), box, fingering, Marker.ROOT);

    Fretboard backBoard = new Fretboard();
    backBoard.setBox(new Box(frontBoard.getMinFret(), frontBoard.getMaxFret()));
    backBoard.mark(position, getOutlineMarker(chord, scale));
    applyParticularities(frontBoard, backBoard, chord, scale);
    validator.validate(frontBoard, backBoard);
    Supplier<BufferedImage> frontImage = () -> new PngFretboardRenderer(frontBoard, false).render();
    Supplier<BufferedImage> backImage = () -> new PngFretboardRenderer(backBoard, true).render();
    Supplier<Part> backMidi = () -> {
      if (playScaleThenChord()) {
        return MidiFretboardRenderer.builder().fretboard(backBoard).backgroundChord(audio).renderBackground(true).renderForeground(true)
            .foregroundIncludesRoot(foregroundIncludesRoot(chord, scale)).build().render();
      }
      return MidiFretboardRenderer.builder().fretboard(backBoard).backgroundChord(audio).renderForeground(true).foregroundIncludesRoot(foregroundIncludesRoot(chord, scale)).build()
          .render();
    };
    validator.validate(backMidi);

    String cardTitle = getCardTitle(chordInfo, scaleInfo);
    return FretboardDiagramCard.builder().title(cardTitle).fretNumber(backBoard.getMinFret()).stringNumber(stringNumber).chordInfo(chordInfo).scaleInfo(scaleInfo)
        .frontImage(frontImage).backImage(backImage).backMidi(backMidi).build();
  }

  protected abstract Note getFrontRoot(Scale chord, Scale scale);

  protected void applyParticularities(Fretboard frontBoard, Fretboard backBoard, Scale chord, Scale scale) {
    // do nothing
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
