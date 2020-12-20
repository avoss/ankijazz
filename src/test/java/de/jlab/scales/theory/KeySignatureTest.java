package de.jlab.scales.theory;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.TestUtils.reviewMarker;
import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMajor;
import static de.jlab.scales.theory.BuiltinScaleType.HarmonicMinor;
import static de.jlab.scales.theory.BuiltinScaleType.Major;
import static de.jlab.scales.theory.BuiltinScaleType.MelodicMinor;
import static de.jlab.scales.theory.KeySignature.fromScale;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.allKeys;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    List<String> scaleNotations = signature.notate(scale);
    List<String> otherNotations = Stream.of(Note.values()).filter(n -> !scale.contains(n)).map(signature::notate).collect(toList());
    assertEquals(Note.values().length, scaleNotations.size() + otherNotations.size());
    scaleNotations.retainAll(otherNotations);
    assertTrue("expected scale notations to be different from non-scale notations: " + scale + " " + signature, scaleNotations.isEmpty());
  }

  private void assertNoDuplicateNotesExist(Scale scale, KeySignature signature) {
    Set<String> set = signature.notate(scale).stream().map(s -> s.replaceAll("x|#|b", "")).collect(Collectors.toSet());
    assertEquals(signature.toString(scale), Math.min(scale.getNumberOfNotes(), CMajor.getNumberOfNotes()), set.size());
  }

  @Test
  public void testChords() {
    Scale dmaj7 = Scales.Cmaj7.transpose(D);
    assertEquals("D F# A C#", KeySignature.fromChord(dmaj7, SHARP).toString(dmaj7));
    //assertEquals("D Gb A Db", KeySignature.fromChord(dmaj7, FLAT).toString(dmaj7));
    
    Scale dbm6 = Scales.Cm6.transpose(Db);
    assertEquals("C# E G# A#", KeySignature.fromChord(dbm6, SHARP).toString(dbm6));
    assertEquals("Db E Ab Bb", KeySignature.fromChord(dbm6, FLAT).toString(dbm6));
    
    Scale gmaj7 = Scales.Cmaj7.transpose(G);
    assertEquals("G B D F#", KeySignature.fromChord(gmaj7, SHARP).toString(gmaj7));
    //assertEquals("G B D Gb", KeySignature.fromChord(gmaj7, FLAT).toString(gmaj7));
    
    Scale gsm6 = Scales.Cm6.transpose(Ab);
    assertEquals("G# B D# F", KeySignature.fromChord(gsm6, SHARP).toString(gsm6));
  }
 
}
