package de.jlab.scales.lily;

import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMajor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor6Pentatonic;
import static de.jlab.scales.theory.BuiltInScaleTypes.Minor7Pentatonic;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinorPentatonic;
import static de.jlab.scales.theory.Scales.CWholeTone;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class LilyScaleTest {
  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor, HarmonicMajor, Minor7Pentatonic, Minor6Pentatonic);

  @Test
  public void testEDorian() {
    ScaleInfo edorian = universe.info(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e4 ~ e1 }");
    assertThat(source).contains("noteNames = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }");
    assertThat(source).contains("midiChord = \\relative e' { <e, b' d g>1 }");
    assertThat(source).contains("\\key d \\major");
  }

  @Test
  public void testDescending() {
    ScaleInfo edorian = universe.info(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian, Direction.DESCENDING).toLily();
    assertThat(source).contains("scaleNotes = \\relative e'' { e4 d4 cs4 b4 a4 g4 fs4 e4 ~ e1 }");
  }
  
  @Test
  public void testGbFsMajor() {
    Scale gbmajor = CMajor.transpose(Gb);
    List<ScaleInfo> infos = universe.infos(gbmajor);
    assertEquals(2,  infos.size());
    String source = new LilyScale(infos.get(1)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { fs4 gs4 as4 b4 cs4 ds4 es4 fs4 ~ fs1 }");
    assertThat(source).contains("\\key fs \\major");
    source = new LilyScale(infos.get(0)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { gf4 af4 bf4 cf4 df4 ef4 f4 gf4 ~ gf1 }");
    assertThat(source).contains("\\key gf \\major");
  }
  
  @Test
  public void testScalesWithDifferentNumberOfNotes() {
    // 5 notes
    String cMinorPentatonicsource = new LilyScale(universe.info(CMinorPentatonic)).toLily();
    assertThat(cMinorPentatonicsource).contains("scaleNotes = \\relative e' { c4 ef4 f4 g4 bf4 c2. }");
    // 6 notes
    String cWholeToneSource = new LilyScale(universe.info(CWholeTone)).toLily();
    assertThat(cWholeToneSource).contains("scaleNotes = \\relative e' { c4 d4 e4 gf4 af4 bf4 c2 }");
    // 7 notes
    String cMajorSource = new LilyScale(universe.info(CMajor)).toLily();
    assertThat(cMajorSource).contains("scaleNotes = \\relative e' { c4 d4 e4 f4 g4 a4 b4 c4 ~ c1 }");
    // 8 notes
    String cDiminishedSource = new LilyScale(universe.info(CDiminishedHalfWhole)).toLily();
    assertThat(cDiminishedSource).contains("scaleNotes = \\relative e' { c4 df4 ef4 e4 gf4 g4 a4 bf4 c1 }");
  }
  
  @Test
  public void reproduceBug() {
    Scale gbaltered = CMelodicMinor.transpose(G).superimpose(Gb);
    ScaleInfo info = universe.info(gbaltered);
    String source = new LilyScale(info).toLily();
    assertThat(source).contains("\\key f \\major");
  }
  
}
