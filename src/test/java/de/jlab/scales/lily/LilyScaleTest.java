package de.jlab.scales.lily;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.Test;

import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.HarmonicMinor;
import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.BuiltInScaleTypes.MelodicMinor;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleUniverse;

public class LilyScaleTest {
  private static ScaleUniverse universe = new ScaleUniverse(true, Major, HarmonicMinor, MelodicMinor);

  @Test
  public void testEDorian() {
    Scale edorian = CMajor.transpose(D).superimpose(E);
    String source = new LilyScale(universe.info(edorian), Accidental.SHARP).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e~ e1 }");
    assertThat(source).contains("chordNotes = \\relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }");
    assertThat(source).contains("\\key d \\major");
  }

  @Test
  public void testGbMajor() {
    Scale gbmajor = CMajor.transpose(Gb);
    String source = new LilyScale(universe.info(gbmajor), Accidental.SHARP).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { fs4 gs4 as4 b4 cs4 ds4 es4 fs~ fs1 }");
    assertThat(source).contains("\\key fs \\major");
    source = new LilyScale(universe.info(gbmajor), Accidental.FLAT).toLily();
    assertThat(source).contains("scaleNotes = \\relative e' { gf4 af4 bf4 cf4 df4 ef4 f4 gf~ gf1 }");
    assertThat(source).contains("\\key gf \\major");
  }
}
