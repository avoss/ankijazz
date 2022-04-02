package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.CDiminishedHalfWhole;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.allKeys;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import de.jlab.scales.theory.IntervalAnalyzer.Result;

public class IntervalAnalyzerTest {

  @Test
  public void testIntervalMapFromScale() {
    assertIntervals("1 2 3 4 5 6 7", allKeys(CMajor));
    assertIntervals("1 2 b3 4 5 6 b7", allKeys(CMajor.superimpose(D)));
    assertIntervals("1 b2 b3 4 5 b6 b7", allKeys(CMajor.superimpose(E)));
    assertIntervals("1 2 3 #4 5 6 7", allKeys(CMajor.superimpose(F)));
    assertIntervals("1 2 3 4 5 6 b7", allKeys(CMajor.superimpose(G)));
    assertIntervals("1 2 b3 4 5 b6 b7", allKeys(CMajor.superimpose(A)));
    assertIntervals("1 b2 b3 4 b5 b6 b7", allKeys(CMajor.superimpose(B)));
    assertIntervals("1 2 b3 4 5 6 7", allKeys(CMelodicMinor));
    assertIntervals("1 2 3 #4 5 6 b7", allKeys(CMelodicMinor.superimpose(F)));
    assertIntervals("1 b2 b3 b4 b5 b6 b7", allKeys(CMelodicMinor.superimpose(B)));
    assertIntervals("1 2 b3 4 5 b6 7", allKeys(CHarmonicMinor));
    assertIntervals("1 b2 3 4 5 b6 b7", allKeys(CHarmonicMinor.superimpose(G)));
    assertIntervals("1 3 5 b7", allKeys(C7));
  }
  
  private void assertIntervals(String expected, Collection<Scale> scales) {
    for (Scale scale : scales) {
      Result result = new IntervalAnalyzer().analyze(scale);
      assertEquals(expected, result.toString());
    }
  }

  @Test
  public void testIntervalMapFallback() {
    assertIntervals("1 b2 b3 3 b5 5 6 b7", allKeys(CDiminishedHalfWhole));
  }

}
