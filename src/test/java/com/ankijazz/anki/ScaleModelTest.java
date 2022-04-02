package com.ankijazz.anki;

import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Scales.CMajor;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.ankijazz.anki.LilyCard;
import com.ankijazz.anki.ModesPracticeCard;
import com.ankijazz.anki.ScaleModel;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleUniverse;

public class ScaleModelTest {
  
  @Test
  public void testWriteAssets() throws IOException {
    Scale bb7 = CMajor.transpose(Eb).superimpose(Bb);
    ScaleModel model = new ScaleModel(ScaleUniverse.MODES.findFirstOrElseThrow(bb7));
    LilyCard card = new ModesPracticeCard(model);
    Path dir = Paths.get("build/lily");
    Files.createDirectories(dir);
    card.writeAssets(dir);
    Path ly = dir.resolve(card.getLilyName());
    List<String> lines = Files.readAllLines(ly);
    assertThat(lines.toString()).contains("bf4 c4 d4 ef4 f4 g4 af4");
  }
  
}
