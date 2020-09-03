package de.jlab.scales.lily;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class LilyScaleTest {
  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor, HarmonicMajor, Minor7Pentatonic, Minor6Pentatonic);

  @Test
  public void testEDorian() {
    ScaleInfo edorian = universe.info(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian, edorian.getKeySignature()).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e4 ~ e1 }");
    assertThat(source).contains("noteNames = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }");
    assertThat(source).contains("midiChord = \\relative e' { <e, b' d g>1 }");
    assertThat(source).contains("lilyChord = \\relative e' { <e g b d>1 }");
    assertThat(source).contains("\\key d \\major");
  }

  @Test
  public void testDescending() {
    ScaleInfo edorian = universe.info(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian, edorian.getKeySignature(), true).toLily();
    assertThat(source).contains("scaleNotes = \\relative e'' { e4 d4 cs4 b4 a4 g4 fs4 e4 ~ e1 }");
  }
  
  @Test
  public void testGbMajor() {
    Scale gbmajor = CMajor.transpose(Gb);
    String source = new LilyScale(universe.info(gbmajor), KeySignature.fromScale(gbmajor, Gb, SHARP)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { fs4 gs4 as4 b4 cs4 ds4 es4 fs4 ~ fs1 }");
    assertThat(source).contains("\\key fs \\major");
    source = new LilyScale(universe.info(gbmajor), KeySignature.fromScale(gbmajor, Gb, FLAT)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { gf4 af4 bf4 cf4 df4 ef4 f4 gf4 ~ gf1 }");
    assertThat(source).contains("\\key gf \\major");
  }
  
  @Test
  public void testScalesWithDifferentNumberOfNotes() {
    // 5 notes
    String cMinorPentatonicsource = new LilyScale(universe.info(CMinorPentatonic), KeySignature.fromScale(CMajor.transpose(Eb), Eb, FLAT)).toLily();
    assertThat(cMinorPentatonicsource).contains("scaleNotes = \\relative e' { c4 ef4 f4 g4 bf4 c2. }");
    // 6 notes
    String cWholeToneSource = new LilyScale(universe.info(CWholeTone), KeySignature.fallback(CWholeTone, FLAT)).toLily();
    assertThat(cWholeToneSource).contains("scaleNotes = \\relative e' { c4 d4 e4 gf4 af4 bf4 c2 }");
    // 7 notes
    String cMajorSource = new LilyScale(universe.info(CMajor), KeySignature.fromScale(CMajor, C, SHARP)).toLily();
    assertThat(cMajorSource).contains("scaleNotes = \\relative e' { c4 d4 e4 f4 g4 a4 b4 c4 ~ c1 }");
    // 8 notes
    String cDiminishedSource = new LilyScale(universe.info(CDiminishedHalfWhole), KeySignature.fallback(CDiminishedHalfWhole, FLAT)).toLily();
    assertThat(cDiminishedSource).contains("scaleNotes = \\relative e' { c4 df4 ef4 e4 gf4 g4 a4 bf4 c1 }");
  }
  
  @Test
  public void reproduceBug() {
    Scale gbaltered = CMelodicMinor.transpose(G).superimpose(Gb);
    ScaleInfo info = universe.info(gbaltered);
    String source = new LilyScale(info, info.getKeySignature()).toLily();
    assertThat(source).contains("\\key f \\major");
  }
  
}
