package de.jlab.scales.lily;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CMajor;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class LilyScaleTest {
  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor);

  @Test
  public void testEDorian() {
    ScaleInfo edorian = universe.info(CMajor.transpose(D).superimpose(E));
    String source = new LilyScale(edorian, edorian.getKeySignature()).toLily();
    //assertEquals("", source);
    assertThat(source).contains("scaleNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e~ e1 }");
    assertThat(source).contains("noteNames = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }");
    assertThat(source).contains("midiChord = \\relative e' { <e, b' d g>1 }");
    assertThat(source).contains("lilyChord = \\relative e' { <e g b d>1 }");
    assertThat(source).contains("\\key d \\major");
  }

  @Test
  public void testGbMajor() {
    Scale gbmajor = CMajor.transpose(Gb);
    String source = new LilyScale(universe.info(gbmajor), KeySignature.of(Gb, SHARP)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { fs4 gs4 as4 b4 cs4 ds4 es4 fs~ fs1 }");
    assertThat(source).contains("\\key fs \\major");
    source = new LilyScale(universe.info(gbmajor), KeySignature.of(Gb, FLAT)).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { gf4 af4 bf4 cf4 df4 ef4 f4 gf~ gf1 }");
    assertThat(source).contains("\\key gf \\major");
  }
}
