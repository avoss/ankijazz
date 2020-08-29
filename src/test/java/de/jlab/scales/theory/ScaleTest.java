package de.jlab.scales.theory;

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
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.CHarmonicMajor;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.Cdim7;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static de.jlab.scales.theory.Scales.CminTriad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class ScaleTest {

  @Test
  public void getNote_should_work_with_negative_index() {
    assertEquals(C, CMajor.getNote(0));
    assertEquals(D, CMajor.getNote(1));
    assertEquals(B, CMajor.getNote(-1));
  }

  @Test
  public void testIsMajor() {
    boolean expected[] = { true, false, false, true, true, false, false };
    for (int i = 0; i < expected.length; i++) {
      Scale s = CMajor.superimpose(CMajor.getNote(i));
      assertEquals(expected[i], s.isMajor());
    }
    assertTrue(CmajTriad.isMajor());
    assertFalse(CminTriad.isMajor());
  }

  @Test
  public void testIsDominant() {
    assertTrue(C7.isDominant());
    assertTrue(C7.transpose(F).isDominant());
    assertFalse(Cmaj7.isDominant());
    assertFalse(Cmaj7.transpose(Db).isDominant());
  }

  @Test
  public void intervals() {
    List<Integer> expected = Arrays.asList(new Integer[] { 2, 2, 1, 2, 2, 2, 1 });
    for (Note n : Note.values()) {
      assertEquals("Intervals do not match for root " + n, expected, CMajor.transpose(n).intervals());
    }
  }

  @Test
  public void testSpell() {
    assertEquals("C D E F G A B", CMajor.toString());
    assertEquals("G A B C D E Gb", CMajor.transpose(G).toString());
    assertEquals("F G A Bb C D E", CMajor.transpose(F).toString());
  }

  @Test
  public void testChord() {
    assertEquals("C E G B", CMajor.getChord(0).toString());
    assertEquals(CMajor.getChord(0), Cmaj7);
    assertEquals("D F A", CMajor.getChord(1, 3).toString());
    assertEquals("D F A C", CMajor.getChord(1).toString());
  }

  @Test
  public void testContainsNote() {
    assertTrue(CMelodicMinor.contains(Eb));
    assertTrue(CMelodicMinor.contains(Eb.transpose(12)));
    assertTrue(CMelodicMinor.contains(Eb.transpose(-12)));
    assertFalse(CMelodicMinor.contains(E));
  }

  @Test
  public void testContainsScale() {
    assertTrue(CMelodicMinor.contains(CMelodicMinor));
    assertFalse(CMelodicMinor.contains(CMajor));
    assertTrue(CMajor.contains(new Scale(D, F, A, C)));
  }

  @Test
  public void testTranspose() {
    assertEquals(C, C.root());
    assertEquals(Db, C.flat9());
    assertEquals(D, C.nine());
    assertEquals(Eb, C.minor3());
    assertEquals(Eb, C.minor3());
    assertEquals(E, C.major3());
    assertEquals(F, C.four());
    assertEquals(Gb, C.flat5());
    assertEquals(G, C.five());
    assertEquals(Ab, C.sharp5());
    assertEquals(A, C.major6());
    assertEquals(Bb, C.flat7());
    assertEquals(B, C.sharp7());
  }

  @Test
  public void testAsChord() {
    assertEquals("CΔ7", Cmaj7.superimpose(C).asChord());
    assertEquals("Am79", Cmaj7.superimpose(A).asChord());
    assertEquals("Eb6", Cm7.superimpose(Eb).asChord());
    assertEquals("Dm7", Cm7.transpose(2).asChord());
    assertEquals("Dm7", Cm7.transpose(2).superimpose(D).asChord());
    assertEquals("Em7", CMajor.getChord(2).asChord());
    assertEquals("CΔ79", CMajor.getChord(2).superimpose(C).asChord());
    assertEquals("Bm7b5", CMajor.getChord(6).asChord());
    assertEquals("Db7#5b9", CMajor.getChord(6).superimpose(Db).asChord());
    assertEquals("Co7", Cdim7.superimpose(C).superimpose(C).asChord());
    assertEquals("Gb7b5b9", C7.superimpose(Gb).asChord());
    assertEquals("C7b9", Cdim7.transpose(1).superimpose(C).asChord());
  }

  @Test
  public void fromChord() {
    fromChord("CΔ7");
    fromChord("C7");
    fromChord("F#", Accidental.SHARP);
    fromChord("F7#5", Accidental.SHARP);
    fromChord("Eb7#5b913");
    fromChord("Csus7913");
    fromChord("Cm7");
    fromChord("Cm79");
    fromChord("C6");
    fromChord("C7b5#9");
    fromChord("Co");
    fromChord("Co7");
  }

  @Test
  public void allScaleChords() {
    Scale[] scales = { CMajor, CMelodicMinor, CHarmonicMinor, CHarmonicMajor };
    for (Scale scale : scales) {
      for (Note root : Note.values()) {
        Scale transposed = scale.transpose(root);
        for (Scale chord : transposed.getChords(4)) {
          assertThat(chord.asChord(FLAT)).isEqualTo(Scales.parseChord(chord.asChord(FLAT)).asChord(FLAT));
          assertThat(chord.asChord(Accidental.SHARP)).isEqualTo(Scales.parseChord(chord.asChord(SHARP)).asChord(SHARP));
        }
      }
    }
  }

  private void fromChord(String string) {
    fromChord(string, Accidental.FLAT);
  }

  private void fromChord(String expected, Accidental accidental) {
    Scale chord = Scales.parseChord(expected);
    String actual = chord.asChord(accidental);
    assertEquals(expected, actual);
  }

  @Test(expected = ParseChordException.class)
  public void invalidChord1() {
    Scales.parseChord("xm7");
  }

  @Test(expected = ParseChordException.class)
  public void invalidChord2() {
    Scales.parseChord("Cm7x");
  }

  @Test
  public void transposeNote() {
    Scale scale = CMajor.transpose(2);
    assertEquals(Note.D, scale.getRoot());
    scale = scale.transpose(E);
    assertEquals(Note.E, scale.getRoot());
    scale = scale.transpose(C);
    assertEquals(CMajor, scale);
  }

  @Test
  public void testGetNoteCDorian() {
    Note[] notes = { D, E, F, G, A, B, C };
    Scale scale = CMajor.superimpose(D);
    iteratorTest(notes, scale);
  }

  private void iteratorTest(Note[] notes, Scale scale) {
    Iterator<Note> iter = scale.iterator();
    for (int i = 0; i < notes.length; i++) {
      assertEquals(notes[i], scale.getNote(i));
      assertEquals(notes[i], iter.next());
      assertEquals(i, scale.indexOf(notes[i]));
    }
    assertFalse(iter.hasNext());
  }

  @Test
  public void stream() {
    Scale ddorian = CMajor.superimpose(D);
    List<Note> expected = Arrays.asList(D, E, F, G, A, B, C);
    List<Note> actual = ddorian.stream().collect(Collectors.toList());
    assertEquals(expected, actual);
  }
}
