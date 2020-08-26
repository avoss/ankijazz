package de.jlab.scales.anki;

import static de.jlab.scales.anki.ScaleCard.SHUFFLE;
import static de.jlab.scales.theory.Accidental.*;

import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Note.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import static org.assertj.core.api.Assertions.*;
public class ScaleCardTest {

  @Test
  public void testPriority() {
    assertThat(card(CMajor, SHARP).getPriority()).isBetween(0, SHUFFLE);
    assertThat(card(CMajor.transpose(F), FLAT).getPriority()).isBetween(1,  1 + SHUFFLE);
    assertThat(card(CMajor.transpose(G), SHARP).getPriority()).isBetween(1, 1 + SHUFFLE);
    assertThat(card(CMajor.transpose(D), SHARP).getPriority()).isBetween(2, 2 + SHUFFLE);
    assertThat(card(CMajor.transpose(Bb), FLAT).getPriority()).isBetween(2, 2 + SHUFFLE);
    assertThat(card(CMajor.transpose(Gb), FLAT).getPriority()).isBetween(6, 6 + SHUFFLE);
  }

  @Test
  public void testWriteAssets() throws IOException {
    Scale bb7 = CMajor.transpose(Eb).superimpose(Bb);
    ScaleCard c = card(bb7, FLAT);
    Path dir = Paths.get("build/lily");
    Files.createDirectories(dir);
    c.writeAssets(dir);
    Path ly = dir.resolve(c.lilyName());
    List<String> lines = Files.readAllLines(ly);
    assertThat(lines.toString()).contains("bf4 c4 d4 ef4 f4 g4 af4");
  }
  
  private ScaleCard card(Scale scale, Accidental accidental) {
    return new ScaleCard(scale, KeySignature.of(scale.getRoot(), accidental));
  }


}
