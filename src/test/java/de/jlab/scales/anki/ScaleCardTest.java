package de.jlab.scales.anki;

import static de.jlab.scales.anki.ScaleCard.SHUFFLE;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.Scales.CMajor;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
public class ScaleCardTest {

  @Test
  public void testPriority() {
    assertThat(card(CMajor).getPriority()).isBetween(0, SHUFFLE);
    assertThat(card(CMajor.transpose(F)).getPriority()).isBetween(1,  1 + SHUFFLE);
    assertThat(card(CMajor.transpose(G)).getPriority()).isBetween(1, 1 + SHUFFLE);
    assertThat(card(CMajor.transpose(D)).getPriority()).isBetween(2, 2 + SHUFFLE);
    assertThat(card(CMajor.transpose(Bb)).getPriority()).isBetween(2, 2 + SHUFFLE);
    assertThat(card(CMajor.transpose(Gb)).getPriority()).isBetween(6, 6 + SHUFFLE);
  }

  @Test
  public void testWriteAssets() throws IOException {
    Scale bb7 = CMajor.transpose(Eb).superimpose(Bb);
    ScaleCard c = card(bb7);
    Path dir = Paths.get("build/lily");
    Files.createDirectories(dir);
    c.writeAssets(dir);
    Path ly = dir.resolve(c.lilyName());
    List<String> lines = Files.readAllLines(ly);
    assertThat(lines.toString()).contains("bf4 c4 d4 ef4 f4 g4 af4");
  }
  
  private ScaleCard card(Scale scale) {
    return new ScaleCard(scale, KeySignature.fromMajorScale(scale));
  }


}
