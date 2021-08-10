package de.jlab.scales.theory;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.theory.Scales.allKeys;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.TestUtils;

public class ChrisBollinger {

  static class AsciiList {
    List<String> strings = new ArrayList<>();
    public void add(String s) {
      strings.add(s.replace("Δ", "Maj"));
    }
    public List<String> asList() {
      return strings;
    }
  }
  
  @Test
  public void chrisBollinger() {
    AsciiList actual = new AsciiList();
    for (Scale prototype : Scales.allChords()) {
      ScaleInfo protoInfo = ScaleUniverse.CHORDS.findFirstOrElseThrow(prototype);
      actual.add("-------------------------------------------");
      actual.add(protoInfo.getScaleName());
      actual.add("-------------------------------------------");
      for (Scale chord : allKeys(prototype)) {
        for (ScaleInfo info : ScaleUniverse.CHORDS.infos(chord)) {
          String marker = TestUtils.reviewMarker(chord, info.getKeySignature());
          actual.add(format("%8s = %-18s %s", info.getScaleName(), info.getKeySignature().notate(chord.stackedThirds()), marker));
        }
      }
    }
    assertFileContentMatches(actual.asList(), getClass(), "chrisBollinger.txt");
  }
}
