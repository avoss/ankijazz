package de.jlab.scales.anki;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PlayModesDeckTest {

  @Test
  public void testHtml() {
    Path path = Paths.get("build", "anki", "html");
   // new PlayModesDeck().writeHtml(path);
    //FIXME assert missing 
  }

}
