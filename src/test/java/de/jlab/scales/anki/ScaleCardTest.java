package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltInScaleTypes.Major;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.Eb;
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
import de.jlab.scales.theory.ScaleUniverse;

public class ScaleCardTest {
  
  static ScaleUniverse universe = new ScaleUniverse(true, Major);
  
  @Test
  public void testWriteAssets() throws IOException {
    Scale bb7 = CMajor.transpose(Eb).superimpose(Bb);
    ScaleModel c = card(bb7);
    Path dir = Paths.get("build/lily");
    Files.createDirectories(dir);
    c.writeAssets(dir);
    Path ly = dir.resolve(c.getLilyName());
    List<String> lines = Files.readAllLines(ly);
    assertThat(lines.toString()).contains("bf4 c4 d4 ef4 f4 g4 af4");
  }
  
  private ScaleModel card(Scale scale) {
    return new ScaleModel(universe.info(scale), false);
  }


}
