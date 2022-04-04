package com.ankijazz.theory;

import static com.ankijazz.theory.BuiltinChordType.AugmentedTriad;
import static com.ankijazz.theory.BuiltinChordType.Diminished7;
import static com.ankijazz.theory.BuiltinChordType.Dominant7flat13;
import static com.ankijazz.theory.BuiltinChordType.Dominant7sus4;
import static com.ankijazz.theory.BuiltinChordType.Minor7b5;
import static com.ankijazz.theory.Scales.CMajor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ankijazz.theory.DegreeParser.Degree;
import com.ankijazz.theory.DegreeParser.Degrees;

public class DegreeParserTest {
  DegreeParser parser = new DegreeParser();
  
  private Degree d(int index, int offset) {
    return new Degree(index, offset);
  }

  @Test
  public void testParse() {
    assertThat(parser.parse("1 b3 5 b7").getDegrees()).containsExactly(d(0, 0), d(2, -1), d(4, 0), d(6, -1));
    assertThat(parser.parse("1 bb3 #4 x7").getDegrees()).containsExactly(d(0, 0), d(2, -2), d(3, 1), d(6, 2));
    assertThat(parser.parse("xb1").getDegrees()).containsExactly(d(0, 1));
    assertThat(parser.parse("1 #11 b13").getDegrees()).containsExactly(d(0, 0), d(10, 1), d(12, -1));
  }
  
  @Test
  public void testToString() {
    testToString("1 b3 5 b7");
    testToString("1 bb3 #4 x7 b13");
  }
  private void testToString(String formula) {
    assertThat(parser.parse(formula).toString()).isEqualTo(formula);
  }

  @Test
  public void testRelative() {
    Degrees degrees = parser.parse("1 b3 5 b7").relativeTo(CMajor.superimpose(Note.A));
    assertThat(degrees.toString()).isEqualTo("1 3 5 7");
    degrees = degrees.relativeTo(CMajor);
    assertThat(degrees.toString()).isEqualTo("1 b3 5 b7");
    degrees = degrees.relativeTo(CMajor.transpose(Note.E));
    assertThat(degrees.toString()).isEqualTo("1 b3 5 b7");
    
    degrees = parser.parse("1 3 #4 b5 7").relativeTo(CMajor.superimpose(Note.A));
    assertThat(degrees.toString()).isEqualTo("1 #3 #4 b5 #7");

    degrees = parser.parse("1 b3 b5 b7").relativeTo(CMajor.superimpose(Note.A));
    assertThat(degrees.toString()).isEqualTo("1 3 b5 7");
  }
  
  @Test
  public void testAsScaleMajor() {
    Scale chord = parser.parse("1 3 #5 b7").asScale(Note.E);
    assertEquals(Scales.C7sharp5.transpose(Note.E), chord);
  }

  @Test
  public void testAsScaleMinor() {
    Degrees degrees = parser.parse("1 b3 5 7").relativeTo(CMajor.superimpose(Note.A));
    assertEquals("1 3 5 #7", degrees.toString());
    Scale chord = degrees.asScale(Note.B);
    assertEquals(Scales.Cmmaj7.transpose(Note.B), chord);
  }

  @Test
  public void testAsList() {
    List<Note> actual = parser.parse("1 3 5 b7 #9").asList(Note.G);
    assertEquals(List.of(Note.G, Note.B, Note.D, Note.F, Note.Bb), actual);
    
  }
  @Test
  public void playground() {
    print(AugmentedTriad);
    print(Minor7b5);
    print(Diminished7);
    print(Dominant7sus4);
    print(Dominant7flat13);
  }
  
  private void print(BuiltinChordType type) {
    Degrees degrees = parser.parse(type.getFormula());
    System.out.println(String.format("\nType %s (%s):", type.getTypeName(), degrees.asScale(Note.C).isMinor() ? "Minor" : "Major")); 
    for (Note root : Note.values()) {
      System.out.print(spellChord(root, degrees));
    }
  }

  private String spellChord(Note chordRoot, Degrees degrees) {
    StringBuilder sb = new StringBuilder();
    Note majorKey = chordRoot;
    if (degrees.asScale(chordRoot).isMinor()) {
      majorKey = chordRoot.transpose(3);
      degrees = degrees.relativeTo(CMajor.superimpose(-3));
    }
    for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(majorKey)) {
      Map<Note, Accidental> accidentalMap = keySignature.getAccidentalMap();
      Scale scale = degrees.getScale().transpose(chordRoot);
      for (Degree degree : degrees.getDegrees()) {
        Note scaleNote = scale.getNote(degree.getNoteIndexInScale());
        Accidental appliedAccidental = accidentalMap.get(scaleNote);
        Note cmajorNote = appliedAccidental.inverse().apply(scaleNote);
        Accidental newAccidental = Accidental.fromOffset(appliedAccidental.offset() + degree.getOffsetToApply());
        sb.append(cmajorNote.name() + newAccidental.symbol() + " ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
