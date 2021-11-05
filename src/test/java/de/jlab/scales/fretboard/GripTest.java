package de.jlab.scales.fretboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import de.jlab.scales.fretboard.Grip.FrettedNote;
import de.jlab.scales.theory.Note;

public class GripTest {

  @Test
  public void testA5thFretSingleString() {
    Grip grip = Grip.builder().fret(Note.E, 5, '1').build();
    assertEquals(1, grip.getFrettedNotes().size());
    FrettedNote actual = grip.getFrettedNotes().get(0);
    assertEquals(Note.A, actual.getNote());
    assertFrettedNote(actual, Note.E, 5, '1');
  }

  @Test
  public void testNextInversionOnSingleString() {
    Grip grip = Grip.builder().fret(Note.E, 5, '1').build().nextInversion();
    FrettedNote actual = grip.getFrettedNotes().get(0);
    assertFrettedNote(actual, Note.E, 5 + 12, '1');
  }

  @Test
  public void testNextInversionOnTwoStrings() {
    Grip grip = Grip.builder().fret(Note.E, 5, '1').fret(Note.B, 5, '5').build().nextInversion();
    FrettedNote eString = grip.getFrettedNotes().get(0);
    assertFrettedNote(eString, Note.E, 12, '5');
    FrettedNote bString = grip.getFrettedNotes().get(1);
    assertFrettedNote(bString, Note.B, 10, '1');
  }
  
  @Test
  public void testSymbolMap() {
    Map<Note, Character> map = Grip.builder().fret(Note.E, 5, '1').build().createSymbolMap();
    assertThat(map).containsEntry(Note.A, '1');
  }
  
  @Test
  public void testFrettedNoteToString() {
    FrettedNote note = new Grip.FrettedNote(Note.A, 5, 'x');
    assertThat(note.toString(3,  7)).isEqualTo("|---|---|-x-|---|---|");
  }
  
  @Test
  public void testToString() {
    Grip grip = Grip.builder().fret(Note.E, 5, 'x').fret(Note.B, 7, 'y').build();
    assertThat(grip.toString()).isEqualTo("|---|-x-|---|---|---|\n|---|---|---|-y-|---|");
  }

  @Test
  public void testAMinor5thFret() {
    Grip grip = Grip.builder()
        .fret(Note.E, 5, '1')
        .fret(Note.B, 5, '5')
        .fret(Note.G, 5, '3')
        .fret(Note.D, 5, '7')
        .build();
    for (int i = 0; i < 4; i++) {
      System.out.println(grip + "\n");
      grip = grip.nextInversion();
    }
  }

  @Test
  public void testD79Chord() {
    Grip grip = Grip.builder()
        .fret(Note.B, 5, '9')
        .fret(Note.G, 5, '7')
        .fret(Note.D, 4, '3')
        .fret(Note.A, 5, '1')
        .build();
    for (int i = 0; i < 4; i++) {
      System.out.println(grip + "\n");
      grip = grip.nextInversion();
    }
  }

  @Test
  public void testEadd9Chord() {
    Grip grip = Grip.builder()
        .fret(Note.E, 4, '3')
        .fret(Note.B, 5, '1')
        .fret(Note.G, 4, '5')
        .fret(Note.D, 4, '9')
        .build();
    for (int i = 0; i < 4; i++) {
      System.out.println(grip + "\n");
      grip = grip.nextInversion();
    }
  }
  
  private void assertFrettedNote(FrettedNote actual, Note string, int fretNumber, char symbol) {
    assertThat(actual.getString()).isEqualTo(string);
    assertThat(actual.getFretNumber()).isEqualTo(fretNumber);
    assertThat(actual.getSymbol()).isEqualTo(symbol);
  }

}
