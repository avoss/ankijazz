package de.jlab.scales.algo;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Note;

public class NotesPracticeSheet {
  
  static class AccNote {
    private Note note;
    private Accidental accidental;
    public String name;
    public AccNote(Note note, Accidental accidental) {
      this.note = note;
      this.accidental = accidental;
    }
    @Override
    public String toString() {
      return note.getName(accidental);
    }
  }

  private List<AccNote> allNotes() {
    Stream<AccNote> notes = Stream.of(C, D, E, F, G, A, B).map(n -> new AccNote(n, FLAT));
    Stream<AccNote> flats = Stream.of(Db, Eb, Gb, Ab, Bb).map(n -> new AccNote(n, FLAT));
    Stream<AccNote> sharps = Stream.of(Db, Eb, Gb, Ab, Bb).map(n -> new AccNote(n, SHARP));
    return new ArrayList<>(concat(notes, concat(flats,  sharps)).collect(Collectors.toList()));
  }
  
  private void permutate(AccNote prev, List<AccNote> notes) {
    Collections.shuffle(notes);
    while (containsInvalidInterval(prev, notes)) {
      Collections.shuffle(notes);
    }
  }

  private boolean containsInvalidInterval(AccNote prev, List<AccNote> notes) {
    for (AccNote note : notes) {
      if (isInvalidInterval(prev, note)) {
        return true;
      }
      prev = note;
    }
    return false;
  }

  private boolean isInvalidInterval(AccNote a, AccNote b) {
    return a.note == b.note || a.note.isSemitone(b.note);
  }

  int col = 0;
  
  @Test
  @Ignore
  public void leadSheetRandomSmall() {
    // 10 rows, 16 cols = 160 notes in total
    // 7 notes + 5 sharps + 5 flats = 17 notes to practice
    List<AccNote> notes = allNotes();
    col = 0;
    AccNote prev = new AccNote(C, FLAT);
    for (int i = 0; i< 8; i++) {
      permutate(prev, notes);
      print(notes);
      prev = notes.get(notes.size()-1);
    }
  }

  private void print(List<AccNote> notes) {
    notes.stream().forEach(note-> {
      System.out.print(note);
      col = col + 1;
      if (col == 8) {
        System.out.print("\n");
        col = 0;
      } else {
        System.out.print("\t");
      }
    });
  }

  private String toString(List<?> list) {
    return list.stream().map(o -> o.toString()).reduce("", (a, b) -> a + b + "\t");
  }
  

}
