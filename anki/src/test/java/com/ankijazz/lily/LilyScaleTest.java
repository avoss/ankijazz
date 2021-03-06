package com.ankijazz.lily;

import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static com.ankijazz.theory.ScaleUniverse.MODES;
import static com.ankijazz.theory.Scales.CDiminishedHalfWhole;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CMelodicMinor;
import static com.ankijazz.theory.Scales.CMinor7Pentatonic;
import static com.ankijazz.theory.Scales.CWholeTone;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

public class LilyScaleTest {

  @Test
  public void testEDorian() {
    ScaleInfo edorian = MODES.findFirstOrElseThrow(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e4 ~ e1 }");
    assertThat(source).contains("noteNames = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }");
    assertThat(source).contains("midiChord = \\relative e' { <e, b' d g>1 }");
    assertThat(source).contains("\\key d \\major");
  }

  @Test
  public void testDescending() {
    ScaleInfo edorian = MODES.findFirstOrElseThrow(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian, Direction.DESCENDING).toLily();
    assertThat(source).contains("scaleNotes = \\relative e'' { e4 d4 cs4 b4 a4 g4 fs4 e4 ~ e1 }");
  }
  
  @Test
  public void testGbFsMajor() {
    Scale gbmajor = CMajor.transpose(Gb);
    List<ScaleInfo> infos = MODES.infos(gbmajor);
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
    String cMinorPentatonicsource = new LilyScale(MODES.findFirstOrElseThrow(CMinor7Pentatonic)).toLily();
    assertThat(cMinorPentatonicsource).contains("scaleNotes = \\relative e' { c4 ef4 f4 g4 bf4 c2. }");
    // 6 notes
    String cWholeToneSource = new LilyScale(MODES.findFirstOrElseThrow(CWholeTone)).toLily();
    assertThat(cWholeToneSource).contains("scaleNotes = \\relative e' { c4 d4 e4 gf4 af4 bf4 c2 }");
    // 7 notes
    String cMajorSource = new LilyScale(MODES.findFirstOrElseThrow(CMajor)).toLily();
    assertThat(cMajorSource).contains("scaleNotes = \\relative e' { c4 d4 e4 f4 g4 a4 b4 c4 ~ c1 }");
    // 8 notes
    String cDiminishedSource = new LilyScale(MODES.findFirstOrElseThrow(CDiminishedHalfWhole)).toLily();
    assertThat(cDiminishedSource).contains("scaleNotes = \\relative e' { c4 df4 ef4 e4 gf4 g4 a4 bf4 c1 }");
  }
  
  @Test
  public void reproduceBug() {
    Scale gbaltered = CMelodicMinor.transpose(G).superimpose(Gb);
    ScaleInfo info = MODES.findFirstOrElseThrow(gbaltered);
    String source = new LilyScale(info).toLily();
    assertThat(source).contains("\\key f \\major");
  }
  
}
