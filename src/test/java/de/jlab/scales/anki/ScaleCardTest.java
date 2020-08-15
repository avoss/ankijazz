package de.jlab.scales.anki;

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
import de.jlab.scales.theory.Scale;
import static org.assertj.core.api.Assertions.*;

public class ScaleCardTest {

  @Test
  public void testPriority() {
    assertEquals(0, card(CMajor, SHARP).getPriority());
    assertEquals(0, card(CMajor, FLAT).getPriority());
    assertEquals(1, card(CMajor.transpose(F), FLAT).getPriority());
    assertEquals(1, card(CMajor.transpose(G), FLAT).getPriority());
    assertEquals(2, card(CMajor.transpose(Bb), FLAT).getPriority());
    assertEquals(2, card(CMajor.transpose(D), FLAT).getPriority());
    assertEquals(6, card(CMajor.transpose(Gb), FLAT).getPriority());
  }

  @Test
  public void testWriteAssets() throws IOException {
    Scale bb7 = CMajor.transpose(Eb).superimpose(Bb);
    ScaleCard c = card(bb7, FLAT);
    Path dir = Paths.get("build/lily");
    c.writeAssets(dir);
    Path ly = dir.resolve(c.lilyName());
    List<String> lines = Files.readAllLines(ly);
    assertThat(lines.toString()).contains("bf4 c4 d4 ef4 f4 g4 af4");
  }
  
  private ScaleCard card(Scale scale, Accidental accidental) {
    return new ScaleCard(scale, accidental);
  }


}
