package com.ankijazz.theory;

import static com.ankijazz.TestUtils.assertFileContentMatches;
import static com.ankijazz.TestUtils.reviewMarker;
import static com.ankijazz.theory.Accidental.SHARP;
import static com.ankijazz.theory.BuiltinScaleType.HarmonicMajor;
import static com.ankijazz.theory.BuiltinScaleType.HarmonicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Major;
import static com.ankijazz.theory.BuiltinScaleType.MelodicMinor;
import static com.ankijazz.theory.KeySignature.fromScale;
import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.Ab;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Scales.CDiminishedHalfWhole;
import static com.ankijazz.theory.Scales.CHarmonicMinor;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CMelodicMinor;
import static com.ankijazz.theory.Scales.allKeys;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class KeySignatureTest {

 
  @Test
  public void testScaleNotation() throws IOException {
    List<String> actual = new ArrayList<>();
    for (ScaleType type : asList(Major, MelodicMinor, HarmonicMinor, HarmonicMajor)) {
      actual.add("# " + type.getTypeName());
      for (Scale scale : allKeys(type.getPrototype())) {
        for (KeySignature keySignature : type.getKeySignatures(scale.getRoot())) {
          String reviewMarker = reviewMarker(scale, keySignature);
          String notationKey = keySignature.getKeySignatureString();
          String message = format("%2s %15s, Signature: %2s (%d%s), Notation: %s %s", keySignature.notate(scale.getRoot()), type.getTypeName(), notationKey, 
              keySignature.getNumberOfAccidentals(), keySignature.getAccidental().symbol(), keySignature.toString(scale), reviewMarker);
          actual.add(message);
          //System.out.println(message);
          assertNoDuplicateNotesExist(scale, keySignature);
          assertNoDuplicateNotationExist(scale, keySignature);
        }
      }
    }
    assertFileContentMatches(actual, KeySignatureTest.class, "testScaleNotation.txt");
  }
  
  @Test
  public void testNotationMapFromScale() {
    assertNotation("C D E F G A B", CMajor);
    assertNotation("Db Eb F Gb Ab Bb C", CMajor.transpose(Db));
    assertNotation("G# A# B C# D# E Fx", CHarmonicMinor.transpose(Ab));
  }
  
  @Test
  public void testAccidentalMap() {
    Scale scale = CHarmonicMinor.transpose(Ab); // G# A# B C# D# E F##
    Map<Note, Accidental> map = fromScale(scale).getAccidentalMap();
    assertEquals(Accidental.NONE, map.get(B));
    assertEquals(Accidental.SHARP, map.get(Ab));
    assertEquals(Accidental.DOUBLE_SHARP, map.get(G));
  }

  @Test
  public void testNotationMapFallback() {
    assertNotation("C Db Eb E Gb G A Bb", CDiminishedHalfWhole);
  }
  
  private void assertNotation(String expected, Scale scale) {
    assertEquals(expected, fromScale(scale).toString(scale));
  }

  @Test
  public void testAMelodicMinorBug() {
    KeySignature keySignature = fromScale(CMelodicMinor.transpose(A), B, SHARP);
    assertEquals("G#", keySignature.notate(Ab));
  }
  
  private void assertNoDuplicateNotationExist(Scale scale, KeySignature signature) {
    List<String> scaleNotations = split(signature.toString(scale));
    List<String> otherNotations = Stream.of(Note.values()).filter(n -> !scale.contains(n)).map(signature::notate).collect(toList());
    assertEquals(Note.values().length, scaleNotations.size() + otherNotations.size());
    scaleNotations.retainAll(otherNotations);
    assertTrue("expected scale notations to be different from non-scale notations: " + scale + " " + signature, scaleNotations.isEmpty());
  }

  private List<String> split(String string) {
    return new ArrayList<>(Arrays.asList(string.split(" +")));
  }

  private void assertNoDuplicateNotesExist(Scale scale, KeySignature signature) {
    Set<String> set = split(signature.toString(scale)).stream().map(s -> s.replaceAll("x|#|b", "")).collect(Collectors.toSet());
    assertEquals(signature.toString(scale), Math.min(scale.getNumberOfNotes(), CMajor.getNumberOfNotes()), set.size());
  }

  @Test
  public void testHasAccidental() {
    BuiltinScaleType.MelodicMinor.getPrototype().transpose(Note.Gb);
    Set<KeySignature> keySignatures = BuiltinScaleType.MelodicMinor.getKeySignatures(Note.Gb);
    assertEquals(1, keySignatures.size());
    KeySignature keySignature = keySignatures.iterator().next();
    assertEquals("E#", keySignature.notate(Note.F));
    assertTrue(keySignature.hasAccidental(Note.F));
    assertEquals("A", keySignature.notate(Note.A));
    assertFalse(keySignature.hasAccidental(Note.A));
  }
}
