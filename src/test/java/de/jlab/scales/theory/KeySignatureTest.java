package de.jlab.scales.theory;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.DiminishedHalfWhole;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.WholeTone;
import static de.jlab.scales.theory.KeySignature.fromScale;
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
  public void testResultInitialize() {
    assertResult(C, SHARP, 0);
    assertResult(G, SHARP, 1, F);

    assertResult(D, SHARP, 2, F, C);
    assertResult(D, SHARP, 2, C, F);
    assertResult(D, SHARP, 2, F, C, D);

    assertResult(A, SHARP, 3, F, C, G);
    assertResult(A, SHARP, 3, F, C, G, A, E);
    assertResult(A, SHARP, 3, A, C, G, F, E);

    assertResult(E, SHARP, 4, F, C, G, D);
    assertResult(E, SHARP, 4, F, C, G, D);
    assertResult(B, SHARP, 5, F, C, G, D, A);
    assertResult(Gb, SHARP, 6, F, C, G, D, A, E);

    assertResult(F, FLAT, 1, B);
    assertResult(F, FLAT, 1, B, D);
    assertResult(Bb, FLAT, 2, B, E);
    assertResult(Eb, FLAT, 3, B, E, A);
    assertResult(Ab, FLAT, 4, B, E, A, D);
    assertResult(Db, FLAT, 5, B, E, A, D, G);
    assertResult(Gb, FLAT, 6, B, E, A, D, G, C);
  }

  private void assertResult(Note expectedRoot, Accidental accidental, int expectedNumberOfAccidentalsInKeySignature, Note... notesWithAccidental) {
    Analyzer.Result r = new Analyzer.Result(CMajor, accidental);
    r.getMajorNotesWithAccidental().addAll(asList(notesWithAccidental));
    r.initialize();
    assertEquals(expectedRoot, r.getRoot());
    assertEquals(expectedNumberOfAccidentalsInKeySignature, r.getNumberOfAccidentalsInKeySignature());
  }
 
  @Test
  public void testChordNotation() throws IOException {
    List<String> actual = new ArrayList<>();
    for (ScaleType type : asList(Major, MelodicMinor, HarmonicMinor, HarmonicMajor)) {
      actual.add(format("# %s", type.getTypeName()));
      for (Scale scale : allKeys(type.getPrototype())) {
        KeySignature signature = fromScale(scale);
        actual.add(format("## %s %s", signature.notate(scale.getRoot()), type.getTypeName()));
        for (Scale chord : scale.getChords(4)) {
          String message = format("%10s %s", chord.asChord(signature.getAccidental()), signature.toString(chord));
          System.out.println(message);
          actual.add(message);
          
        }
      }
    }
    assertFileContentMatches(actual, KeySignatureTest.class, "testChordNotation.txt");
  }
  
  @Test
  public void testScaleNotation() throws IOException {
    List<String> actual = new ArrayList<>();
    for (ScaleType type : asList(Major, MelodicMinor, HarmonicMinor, HarmonicMajor, DiminishedHalfWhole, WholeTone)) {
      actual.add("# " + type.getTypeName());
      for (Scale scale : allKeys(type.getPrototype())) {
        KeySignature signature = fromScale(scale);
        String inverseAccidentalsHint = hasInverseAccidentals(scale, signature) ? " (found b and #)" : "";
        String message = format("%2s %15s, Signature: %2s (%d%s), Notation: %s%s", signature.notate(scale.getRoot()), type.getTypeName(), signature.notateKey(), signature.getNumberOfAccidentals(),
            signature.getAccidental().symbol(), signature.toString(scale), inverseAccidentalsHint);
        actual.add(message);
        System.out.println(message);
        assertNoDuplicateNotesExist(scale, signature);
        assertNoDuplicateNotationExist(scale, signature);
      }
    }

    assertFileContentMatches(actual, KeySignatureTest.class, "testScaleNotation.txt");
  }

  @Test
  public void testAccidentalFromScale() {
    assertSignature("Major Scale", CMajor, SHARP, G, D, A, E, B);
    assertSignature("Major Scale", CMajor, FLAT, C, F, Bb, Eb, Ab, Db, Gb);
    assertSignature("Melodic Minor", CMelodicMinor, SHARP,  A, D, E, B, Gb);
    assertSignature("Melodic Minor", CMelodicMinor, FLAT, Db, Ab, Eb, Bb, F, C, G);
    assertSignature("Harmonic Minor", CHarmonicMinor, SHARP, A, E, B, Gb, Db);
    assertSignature("Harmonic Minor", CHarmonicMinor, FLAT, Ab, Eb, Bb, F, C, D, G);

  }

  private void assertSignature(String scaleType, Scale cscale, Accidental expected, Note... roots) {
    for (int i = 0; i < roots.length; i++) {
      Scale scale = cscale.transpose(roots[i]);
      KeySignature actual = fromScale(scale);
      String message = format("%2s %15s Signature: %2s (%d%s) [%s]", roots[i].toString(), scaleType, actual.notateKey(), actual.getNumberOfAccidentals(),
          actual.getAccidental().symbol(), actual.toString(scale));
      System.out.println(message);
      assertEquals(message, expected, actual.getAccidental());
    }
  }

  private void assertNoDuplicateNotationExist(Scale scale, KeySignature signature) {
    List<String> scaleNotations = signature.notate(scale);
    List<String> otherNotations = Stream.of(Note.values()).filter(n -> !scale.contains(n)).map(signature::notate).collect(toList());
    assertEquals(Note.values().length, scaleNotations.size() + otherNotations.size());
    scaleNotations.retainAll(otherNotations);
    assertTrue("expected scale notations to be different from non-scale notations: " + scale + " " + signature, scaleNotations.isEmpty());
  }

  private void assertNoDuplicateNotesExist(Scale scale, KeySignature signature) {
    Set<String> set = signature.notate(scale).stream().map(s -> s.replaceAll("#|b", "")).collect(Collectors.toSet());
    assertEquals(signature.toString(scale), Math.min(scale.length(), CMajor.length()), set.size());
  }

  private boolean hasInverseAccidentals(Scale scale, KeySignature signature) {
    String notation = signature.toString(scale);
    return notation.contains("b") && notation.contains("#");
  }

}
