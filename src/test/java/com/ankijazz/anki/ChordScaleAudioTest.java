package de.jlab.scales.anki;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class ChordScaleAudioTest {

  @Test
  public void testCagedModes() {
    test("CagedModes", ChordScaleAudio.cagedModes());
  }

  @Test
  public void testCagedScales() {
    test("CagedScales", ChordScaleAudio.cagedScales());
  }

  @Test
  public void testPentatonicChords() {
    test("PentatonicChords", ChordScaleAudio.pentatonicChords());
  }

  @Test
  public void testPentatonicModes() {
    test("PentatonicModes", ChordScaleAudio.pentatonicModes());
  }

  @Test
  public void testPentatonicScales() {
    test("PentatonicScales", ChordScaleAudio.pentatonicScales());
  }
  
  private void test(String fileName, List<ChordScaleAudio> scales) {
    List<String> actualLines = new ArrayList<>();
    for (ChordScaleAudio csa : scales) {
      String chord = toString(csa.getChord());
      String scale = toString(csa.getScale());
      String audio = toString(csa.getAudio());
      String line = String.format("chord: %s, scale: %s, audio: %s, title: %s, comment: %s", chord, scale, audio, csa.getTitle(), csa.getComment());
      actualLines.add(line);
    }
    TestUtils.assertFileContentMatches(actualLines, getClass(), fileName.concat(".txt"));
  }

  private String toString(Scale scale) {
    List<ScaleInfo> infos = ScaleUniverse.MODES.infos(scale);
    if (!infos.isEmpty()) {
      return infos.get(0).getScaleName();
    }
    infos = ScaleUniverse.PENTAS.infos(scale);
    if (!infos.isEmpty()) {
      return infos.get(0).getScaleName();
    }
    return ScaleUniverse.CHORDS.infos(scale).get(0).getScaleName();
    
  }

}
