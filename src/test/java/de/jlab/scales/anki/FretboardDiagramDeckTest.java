package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.fretboard2.Fretboard;
import de.jlab.scales.fretboard2.Fretboard.MarkedFret;
import de.jlab.scales.fretboard2.GuitarString;
import de.jlab.scales.fretboard2.Marker;
import de.jlab.scales.fretboard2.StringFretboardRenderer;
import de.jlab.scales.midi.MockMidiOut;
import de.jlab.scales.midi.NoteOn;
import de.jlab.scales.midi.Part;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

import static de.jlab.scales.fretboard2.Fretboard.NON_EMPTY;
import static de.jlab.scales.fretboard2.Fretboard.ROOTS_ONLY;
import static de.jlab.scales.fretboard2.StandardTuning.HIGH_E_STRING;
import static de.jlab.scales.fretboard2.StandardTuning.LOW_E_STRING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;

public class FretboardDiagramDeckTest {

  class Validator implements AbstractFretboardGenerator.Validator {

    
    @Override
    public void validate(Fretboard frontBoard, Fretboard backBoard) {
      assertThatFrontBoardRootIsBackBoardRoot(frontBoard, backBoard);
      assertNumberOfRootsOnBackBoard(backBoard);
      assertFrontAndBackHaveSameSize(frontBoard, backBoard);
    }

    @Override
    public void validate(ScaleInfo chordInfo, ScaleInfo scaleInfo) {
      assertThatChordIsContainedInScale(chordInfo.getScale(), scaleInfo.getScale());
    }

    protected void assertFrontAndBackHaveSameSize(Fretboard frontBoard, Fretboard backBoard) {
      assertThat(frontBoard.getMinFret()).isEqualTo(backBoard.getMinFret());
      assertThat(frontBoard.getMaxFret()).isEqualTo(backBoard.getMaxFret());
    }
    
    protected void assertNumberOfRootsOnBackBoard(Fretboard backBoard) {
      List<MarkedFret> roots = backBoard.findMarkedFrets(ROOTS_ONLY);
      assertThat(roots.stream().map(f -> f.getNote()).collect(toSet()).size()).isEqualTo(1);
      if (containsString(roots, HIGH_E_STRING)) {
        assertThat(containsString(roots, LOW_E_STRING)).isTrue();
        assertThat(roots.size()).isEqualTo(3);
      } else {
        assertThat(roots.size()).isEqualTo(2);
      }
    }

    private boolean containsString(List<MarkedFret> roots, int stringNumber) {
      return roots.stream().filter(f -> f.getStringNumber() == stringNumber).findAny().isPresent();
    }

    private void assertThatFrontBoardRootIsBackBoardRoot(Fretboard frontBoard, Fretboard backBoard) {
      List<MarkedFret> frontFrets = frontBoard.findMarkedFrets(ROOTS_ONLY);
      assertEquals(1, frontFrets.size());
      List<MarkedFret> backFrets = backBoard.findMarkedFrets(ROOTS_ONLY);
      assertThat(backFrets).containsAll(frontFrets);
    }

    protected void assertThatChordIsContainedInScale(Scale chord, Scale scale) {
      assertThat(scale.contains(chord)).isTrue();
    }

    @Override
    public void validate(Supplier<Part> backMidi) {
      assertNoDuplicateNotes(backMidi.get());
    }

    private void assertNoDuplicateNotes(Part part) {
      MockMidiOut midiOut = new MockMidiOut();
      part.perform(midiOut);
      int prevPitch = -1;
      int prevChannel = -1;
      for (NoteOn note : midiOut.getNotes()) {
        assertThat(note.getPitch() == prevPitch && note.getChannel() == prevChannel).isFalse();
        prevPitch = note.getPitch();
        prevChannel = note.getChannel();
      }
    }

  }
  
  class Penta3Validator extends Validator {
    @Override
    protected void assertThatChordIsContainedInScale(Scale chord, Scale scale) {
      Set<Note> scaleNotes = scale.asSet();
      scaleNotes.removeAll(chord.asSet());
      assertThat(scaleNotes.size()).isLessThanOrEqualTo(2);
    }
    @Override
    protected void assertNumberOfRootsOnBackBoard(Fretboard backBoard) {
      // ignore
    }
  }
  
  Validator validator = new Validator();
  Validator penta3Validator = new Penta3Validator();
  
  @Test
  public void testPentatonicsLevel3() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel3Generator(penta3Validator);
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "PentatonicsLevel3VisualizeChords.txt");
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), "PentatonicsLevel3VisualizeChords.json");
    TestUtils.writeTo(deck, 0.1);
  }
  
  @Test
  public void testPentatonicsLevel5() {
    CardGenerator<FretboardDiagramCard> generator = new PentatonicsLevel5Generator(validator);
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.writeTo(deck, 0.1);
  }

  @Test
  public void testCagedLevel3() {
    CardGenerator<FretboardDiagramCard> generator = new CagedLevel3Generator(validator);
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.writeTo(deck, 0.1);
  }
}
