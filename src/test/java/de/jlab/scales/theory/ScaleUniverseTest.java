package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.ScaleType.*;
import static de.jlab.scales.theory.ScaleType.HarmonicMinor;
import static de.jlab.scales.theory.ScaleType.Major;
import static de.jlab.scales.theory.ScaleType.MelodicMinor;
import static de.jlab.scales.theory.ScaleType.Minor6Pentatonic;
import static de.jlab.scales.theory.ScaleType.Minor7Pentatonic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class ScaleUniverseTest {

  @Test
  public void testSuperScales() {
    ScaleUniverse universe = new ScaleUniverse(Accidental.SHARP, Major, Minor7Pentatonic);
    for (Scale scale : universe) {
      // System.out.println(scale.getName());
      if (scale.getNotes().size() == 5) {
        assertEquals(3, scale.getSuperScales().size());
        assertEquals(0, scale.getSubScales().size());
      } else if (scale.getNotes().size() == 7) {
        assertEquals(0, scale.getSuperScales().size());
        assertEquals(3, scale.getSubScales().size());
      } else
        fail("invalid # notes");
      if (scale.getRoot() == Db)
        assertTrue(scale.getName().startsWith(("C#")));
    }
  }

  @Test
  public void testPrintSuperScales() {
    ScaleUniverse universe = new ScaleUniverse(Accidental.FLAT, Major, MelodicMinor, HarmonicMinor, Diminished);
    for (Scale scale : universe) {
      // System.out.println(scale.getName());
      if (!scale.getSuperScales().isEmpty()) {
        System.out.println(String.format("%s is contained in:", scale.getName()));
        for (Scale superScale : scale.getSuperScales())
          System.out.println(superScale.getName());
        System.out.println();
      }
    }
  }

  @Test
  public void testPrintFuzzyScalesForChord() {
    Scale chord = new Scale(D, Bb, C, F);
    Set<Note> notes = new TreeSet<Note>(chord.getNotes());
    notes.addAll(chord.transpose(-3).getNotes());
    notes.addAll(chord.transpose(-7).getNotes());
    chord = new Scale(D, notes);
    System.out.println(chord.asScale());
    ScaleUniverse universe = new ScaleUniverse();
    for (Scale scale : universe) {
      Set<Note> intersection = new TreeSet<Note>(chord.getNotes());
      intersection.removeAll(scale.getNotes());
      if (intersection.size() < 2) {
        System.out.println(String.format("%s - not including: %s", scale.getName(), intersection.toString()));
        for (Scale sub : scale.getSubScales())
          System.out.println("  - " + sub.getName());
      }
    }
  }

  @Test
  public void allAboutTriads() {
    Accidental acc = Accidental.FLAT;
    for (ScaleType type : new ScaleType[]{Major, MelodicMinor, HarmonicMinor})
      printTriadsInScale(type, acc);
    for (ScaleType triad : ScaleType.TRIADS) {
      superimposeChord(triad, acc);
      printScalesContainingChord(triad.getScale(), acc);
    }
  }

  private void printTriadsInScale(ScaleType type, Accidental acc) {
    Scale scale = type.getScale();
    System.out.println(String.format("\nTriads of %s %s", scale.getRoot().name(), type.getName()));
    for (Note triadRoot : Note.values()) {
      for (ScaleType triadType : ScaleType.TRIADS) {
        Scale chord = triadType.getScale().transpose(triadRoot);
        if (scale.getNotes().containsAll(chord.getNotes()))
          System.out.println(String.format("  %5s  %5s (%s)", scale.getRoot().romanName(chord.getRoot())  , chord.asChord(acc), chord.asScale()));
      }
    }
  }
  
  @Test
  public void printSus47ChordsInScales() {
    Scale chord = Scales.parseChord("Csus47");
    printPositionsOfChordInScale(chord, ScaleType.Major, Accidental.FLAT);
    
  }
  
  private void printPositionsOfChordInScale(Scale chord, ScaleType type, Accidental acc) {
    Scale scale = type.getScale();
    System.out.println(String.format("\nPositions of %s in %s", chord.asChord(), type.getName()));
    for (int i = 0; i < 12; i++) {
      if (scale.getNotes().containsAll(chord.transpose(i).getNotes()))
        System.out.println(chord.transpose(i).asChord());
    }
  }

  private void printScalesContainingChord(Scale chord, Accidental acc) {
    System.out.println(String.format("Chord %s is contained in:", chord.asChord(acc)));
    for (ScaleType type : new ScaleType[] { Major, MelodicMinor, HarmonicMinor, Minor7Pentatonic, Minor6Pentatonic }) {
      for (Note root : Note.values()) {
        Scale x = chord.transpose(root);
        Scale scale = type.getScale();
        if (scale.getNotes().containsAll(x.getNotes())) {
          System.out.println(String.format("%s %s contains %s at %s", scale.getRoot(), type.getName(), x.asChord(acc),
              chord.getRoot().romanName(root)));
        }
      }
    }
  }

  private void printValidScalesForChord(Scale chord, Accidental acc) {
    System.out.println(String.format("valid scales for chord %s:", chord.asScale()));
    ScaleUniverse universe = new ScaleUniverse(Accidental.FLAT, Major, MelodicMinor, HarmonicMinor, Diminished, WholeTone, Minor7Pentatonic, Minor6Pentatonic);
    
    for (Scale scale : universe.getAllScales()) {
      if (scale.contains(chord)) {
        System.out.println(" " + scale.getName());
        for (Scale sub : scale.getSubScales())
          System.out.println("   > " + sub.getName());
      }
    }
  }
  
  @Test
  public void printValidScalesForMiles() {
    printValidScalesForChord(new Scale(E, Gb, B, E, G, B), Accidental.FLAT);  
//    printValidScalesForChord(new Scale(E, G, B, D, F, A), Accidental.FLAT);  
    printValidScalesForChord(new Scale(D, F, A, D, F, Bb), Accidental.FLAT);  
//    printValidScalesForChord(new Scale(D, F, Bb, Eb, Gb, B), Accidental.FLAT);  
    printValidScalesForChord(new Scale(Eb, Gb, B, E, G, B), Accidental.FLAT);  
  }

  private void superimposeChord(ScaleType type, Accidental acc) {
    Scale chord = type.getScale();
    Note oldRoot = chord.getRoot();
    System.out.println(String.format("\n%2s %s = %s (%s)\n", oldRoot.getName(acc), type.getName(), chord.asChord(),
        chord.asScale()));
    for (Note newRoot : Note.values()) {
      Scale x = chord.transpose(newRoot);
      System.out.print(String.format("%5s : %2s %s over %2s = %10s, Intervals = ", oldRoot.romanName(newRoot),
          newRoot.getName(acc), type.getName(), oldRoot, x.superimpose(oldRoot).asChord()));
      for (Note n : x) {
        System.out.print(String.format("%6s", chord.intervalName(n)));
        // System.out.print(String.format("%6s", oldRoot.romanName(n)));
      }
      System.out.println();
    }
  }

  @Test
  public void printChordNames() {
    Scale chord = new Scale(D, Bb, C, F).transpose(-7);
    System.out.println(String.format("%s = %s", chord.asChord(), chord.asScale()));
    for (Note newRoot : Note.values()) {
      System.out.println(String.format("%s", chord.superimpose(newRoot).asChord()));
    }
  }

  @Test
  public void printChordsOfScale() {
    Scale scale = HarmonicMinor.getScale().transpose(E);
    System.out.println("\nScale " + scale + " contains the following chords: ");
    for (int i = 0; i < scale.getNotes().size(); i++) {
      System.out.println(scale.getChord(i).asChord());
    }
  }

  @Test
  public void printSomeTriads() {
    Scale scale = Major.getScale();
    for (int i = 0; i < scale.getNotes().size(); i++) {
      System.out.print(String.format("%3s: ", scale.getNote(i)));
      for (int k : new int[] { 12, 0, 2, 4 })
        System.out.print(String.format("%8s", scale.getChord(i + k, 3).asChord(),
            scale.getChord(i + k, 3).superimpose(scale.getNote(i)).asChord()));
      System.out.println();
    }
  }

}
