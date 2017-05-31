package de.jlab.scales.algo;

import static java.util.Arrays.asList;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Note;

import static de.jlab.scales.theory.Accidental.*;
import static de.jlab.scales.theory.Note.*;

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
    if (isInvalidInterval(notes.get(0), notes.get(notes.size() - 1))) {
      return true;
    }
    return false;
  }

  private boolean isInvalidInterval(AccNote a, AccNote b) {
    return a.note == b.note || a.note.isSemitone(b.note);
  }
  

  @Test
  public void leadSheetRandomSmall() {
    // 10 rows, 16 cols = 160 notes in total
    // 7 notes + 5 sharps + 5 flats = 17 notes to practice
    List<AccNote> notes = allNotes();
    AccNote prev = new AccNote(C, FLAT);
    for (int i = 0; i< 10; i++) {
      permutate(prev, notes);
      System.out.println(toString(notes));
      prev = notes.get(notes.size()-1);
    }
  }

  private String toString(List<?> list) {
    return list.stream().map(o -> o.toString()).reduce("", (a, b) -> a + b + "\t");
  }
  

}
