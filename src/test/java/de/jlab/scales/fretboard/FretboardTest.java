package de.jlab.scales.fretboard;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinor6Pentatonic;
import static de.jlab.scales.theory.Scales.CMinorPentatonic;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class FretboardTest {

  @Test
  public void testString() {
    GuitarString s = new GuitarString(E, 7);
    s.markFirst(A, 'X');
    assertEquals("|---|---|---|---|-X-|---|---|", s.toString());
    s.markFirst(G, 'G');
    assertEquals("|---|---|-G-|---|-X-|---|---|", s.toString());
  }

  @Test
  public void testFretboard() {
    Fretboard b = new Fretboard(6);
    b.getString(0).markFirst(G, 'G');
    b.getString(4).markFirst(C, 'C');
    final String expected = //
        "|---|---|---|---|---|---|\n" + //
        "|-C-|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|---|\n" + //
        "|---|---|---|---|---|---|\n" + //
        "|---|---|-G-|---|---|---|\n"; //
    assertEquals(expected, b.toString());
  }

  @Test
  public void printMajorScaleBerkley() {
    printFatboard(CMajor, new Berkley(), rootMarker);
  }

  @Test
  public void printDoreanScaleBerkley() {
    printFatboard(CMajor, new Berkley(), new RootMarker(2, 'D'));
  }

  @Test
  public void printLydianScaleBerkley() {
    printFatboard(CMajor, new Berkley(), new RootMarker(4, 'L'));
  }

  @Test
  public void printMixoLydianScaleBerkley() {
    printFatboard(CMajor, new Berkley(), new RootMarker(5, 'M'));
  }

  @Test
  public void printMelodicMinorScaleBerkley() {
    printFatboard(CMelodicMinor, new Berkley(), rootMarker);
  }

  @Test
  @Ignore
  public void print3NotesPerStringForMajorScale() {
    printFatboard(CMajor.transpose(F), threeNotesPerString, rootMarker);
  }

  @Test
  @Ignore
  public void print3NotesPerStringForMelodicMinorScale() {
    printFatboard(CMelodicMinor.transpose(F), threeNotesPerString, rootMarker);
  }

  @Test
  @Ignore
  public void print3NotesPerStringForHarmonicMinorScale() {
    printFatboard(CHarmonicMinor.transpose(F), threeNotesPerString, rootMarker);
  }

  @Test
  @Ignore
  public void print2NotesPerStringForMinorPentatonic() {
    printFatboard(CMinorPentatonic.transpose(5), new NotesPerString(2, -2, 5), rootMarker);
  }

  @Test
  @Ignore
  public void print2NotesPerStringForMinor6Pentatonic() {
    printFatboard(CMinor6Pentatonic, twoNotesPerString, rootMarker);
  }

  interface Fingering {
    boolean nextString(int index);

    int startIndex();

    int numberOfPositions();
  }

  interface Marker {
    char mark(int index);
  }

  class RootMarker implements Marker {
    int position;
    char marker;

    public RootMarker(int position, char marker) {
      this.position = position;
      this.marker = marker;
    }

    @Override
    public char mark(int index) {
      return this.position == index + 1 ? marker : '\u2022';
    }
  }

  Marker rootMarker = new RootMarker(1, 'R');

  class Berkley implements Fingering {

    @Override
    public boolean nextString(int index) {
      index = index % 14;
      return index % 14 == 12 || index != 13 && index % 3 == 1;
    }

    @Override
    public int startIndex() {
      return -1;
    }

    @Override
    public int numberOfPositions() {
      return 5;
    }

  }

  class NotesPerString implements Fingering {

    private int notesPerString;
    private int startIndex;
    private int numberOfPositions;

    public NotesPerString(int notesPerString, int startIndex, int numberOfPositions) {
      super();
      this.notesPerString = notesPerString;
      this.startIndex = startIndex;
      this.numberOfPositions = numberOfPositions;
    }

    @Override
    public boolean nextString(int index) {
      return (index + 1) % notesPerString == 0;
    }

    @Override
    public int startIndex() {
      return startIndex;
    }

    @Override
    public int numberOfPositions() {
      return numberOfPositions;
    }
  }

  NotesPerString twoNotesPerString = new NotesPerString(2, 0, 5);
  NotesPerString threeNotesPerString = new NotesPerString(3, 0, 7);


  private Fretboard printFatboard(Scale scale, Fingering fingering, Marker marker) {
    Fretboard b = new Fretboard(12, fingering.numberOfPositions() * 2);
    int index = fingering.startIndex();
    Iterator<GuitarString> si = b.getStrings().iterator();
    GuitarString string = si.next();
    while (true) {
      Note note = scale.getNote(index);
      string.markFirst(note, marker.mark(scale.indexOf(note)));
      if ((fingering.nextString(index))) {
        if (!si.hasNext())
          break;
        string = si.next();
      }
      index += 1;
    }
    System.out.println(b);
    return b;
  }

  @Test
  @Ignore
  public void printIntervals() {
    System.out.println(printIntervals(3, "min 3rd"));
    System.out.println(printIntervals(5, "4th"));
    System.out.println(printIntervals(7, "5th"));
  }

  private String printIntervals(int semitones, String title) {
    StringBuilder sb = new StringBuilder();
    sb.append(title).append('\n');
    Fretboard f = new Fretboard(7);
    for (GuitarString string : f.getStrings()) {
      f.clear();
      Note root = string.getRoot().transpose(4);
      string.markFirst(root, 'R');
      Note interval = root.transpose(semitones);
      for (GuitarString other : f.getStrings()) {
        other.markFirst(interval, 'o');
      }
      sb.append(f.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
  
  @Test
  public void printMajorTriads() {
    Fretboard f = new Fretboard(36);
    for (GuitarString s : f.getStrings()) {
      s.markAll(C, '1');
      s.markAll(E, '3');
      s.markAll(G, '5');
    }
    System.out.println(f);
  }
  
}
