package com.ankijazz.anki;

import static com.ankijazz.fretboard.Fretboard.ROOTS_ONLY;
import static com.ankijazz.fretboard.StandardTuning.HIGH_E_STRING;
import static com.ankijazz.fretboard.StandardTuning.LOW_E_STRING;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.fretboard.Fretboard.MarkedFret;
import com.ankijazz.midi.MockMidiOut;
import com.ankijazz.midi.NoteOn;
import com.ankijazz.midi.Part;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

public class FretboardDiagramDeckTest {

  class Validator implements AbstractFretboardGenerator.Validator {

    Set<String> fretboards = new HashSet<>();
    
    @Override
    public void validate(Fretboard frontBoard, Fretboard backBoard) {
      assertThatFrontBoardRootIsBackBoardRoot(frontBoard, backBoard);
      assertNumberOfRootsOnBackBoard(backBoard);
      assertFrontAndBackHaveSameSize(frontBoard, backBoard);
      assertNoDuplicateQuestionAnswerPair(frontBoard, backBoard);
    }

    private void assertNoDuplicateQuestionAnswerPair(Fretboard frontBoard, Fretboard backBoard) {
      String string = frontBoard.toString().concat(backBoard.toString());
      assertThat(fretboards.add(string)).withFailMessage(() -> "Duplicate boards: " + backBoard.toString()).isTrue();
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
  public void testPentatonic1ScalesFretboards() {
    assertDeck("Pentatonic1ScalesFretboards.json", new Pentatonic1ScalesFretboards(validator));
  }

  private FretboardDiagramDeck assertDeck(String fileName, CardGenerator<FretboardDiagramCard> generator) {
    FretboardDiagramDeck deck = new FretboardDiagramDeck(generator);
    TestUtils.assertFileContentMatches(deck.getJson(), getClass(), fileName);
    return deck;
  }
  
  @Test
  public void testPentatonic3ChordsFretboards() {
    FretboardDiagramDeck deck = assertDeck("Pentatonic3ChordsFretboards.json", new Pentatonic3ChordsFretboards(penta3Validator));
    TestUtils.assertFileContentMatches(deck.getCsv(), getClass(), "Pentatonic3ChordsFretboards.txt");
  }
  
  @Test
  public void testPentatonic5ModesFretboards() {
    assertDeck("Pentatonic5ModesFretboards.json",  new Pentatonic5ModesFretboards(validator));
  }

  @Test
  public void testCaged1ScalesFretboards() {
    assertDeck("Caged1ScalesFretboards.json",  new Caged1ScalesFretboards(validator));
  }

  @Test
  public void testCaged3ModesFretboards() {
    assertDeck("Caged3ModesFretboards.json", new Caged3ModesFretboards(validator));
  }
  
  @Test
  public void testCaged5ArpeggiosFretboards() {
    assertDeck("Caged5ArpeggiosFretboards.json", new Caged5ArpeggiosFretboards(validator));
  }
  
}
