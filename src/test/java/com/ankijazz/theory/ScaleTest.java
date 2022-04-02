package com.ankijazz.theory;

import static com.ankijazz.theory.Accidental.FLAT;
import static com.ankijazz.theory.Accidental.SHARP;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.F;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static com.ankijazz.theory.Scales.C7;
import static com.ankijazz.theory.Scales.CHarmonicMajor;
import static com.ankijazz.theory.Scales.CHarmonicMinor;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CMelodicMinor;
import static com.ankijazz.theory.Scales.Cdim7;
import static com.ankijazz.theory.Scales.Cm7;
import static com.ankijazz.theory.Scales.Cmaj7;
import static com.ankijazz.theory.Scales.CmajTriad;
import static com.ankijazz.theory.Scales.CminTriad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ankijazz.theory.Accidental;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.ParseChordException;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Scales;

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
    assertEquals(Bb, C.minor7());
    assertEquals(B, C.major7());
  }

  @Test
  public void testAsChord() {
    assertEquals("CΔ7", Cmaj7.superimpose(C).asChord());
    assertEquals("Am9", Cmaj7.superimpose(A).asChord());
    assertEquals("Am9", Cmaj7.superimpose(A.ordinal()).asChord());
    assertEquals("Eb6", Cm7.superimpose(Eb).asChord());
    assertEquals("Dm7", Cm7.transpose(2).asChord());
    assertEquals("Dm7", Cm7.transpose(2).superimpose(D).asChord());
    assertEquals("Em7", CMajor.getChord(2).asChord());
    assertEquals("CΔ9", CMajor.getChord(2).superimpose(C).asChord());
    assertEquals("Bm7b5", CMajor.getChord(6).asChord());
    assertEquals("Db7#5b9", CMajor.getChord(6).superimpose(Db).asChord());
    assertEquals("Cdim7", Cdim7.superimpose(C).superimpose(C).asChord());
    assertEquals("Gb7b5b9", C7.superimpose(Gb).asChord());
    assertEquals("C7b9", Cdim7.transpose(1).superimpose(C).asChord());
  }

  @Test
  public void fromChord() {
    fromChord("CΔ7");
    fromChord("C7");
    fromChord("F#", Accidental.SHARP);
    fromChord("F7#5", Accidental.SHARP);
    fromChord("Cm7");
    fromChord("Cm9");
    fromChord("C6");
    fromChord("C7b5#9");
    fromChord("Cdim");
    fromChord("Cdim7");
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
