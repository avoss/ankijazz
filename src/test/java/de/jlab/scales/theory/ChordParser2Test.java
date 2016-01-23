package de.jlab.scales.theory;

import static de.jlab.scales.theory.ScaleType.Augmented;
import static de.jlab.scales.theory.ScaleType.Diminished;
import static de.jlab.scales.theory.ScaleType.HarmonicMajor;
import static de.jlab.scales.theory.ScaleType.HarmonicMinor;
import static de.jlab.scales.theory.ScaleType.Major;
import static de.jlab.scales.theory.ScaleType.MelodicMinor;
import static de.jlab.scales.theory.ScaleType.WholeTone;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ChordParser2Test {
  
  ScaleType[] scaleTypes = {
      Major,
      MelodicMinor,
      HarmonicMajor,
      HarmonicMinor,
      Augmented,
      Diminished,
      WholeTone,
    };


  @Test
  public void testAllChordsFromAllScales() {
    testChords(Accidental.FLAT);
    testChords(Accidental.SHARP);
  }

  private void testChords(Accidental accidental) {
    for (Scale scale : new ScaleUniverse(accidental, scaleTypes)) {
      testChords(accidental, scale, 3);
      testChords(accidental, scale, 4);
    }
  }

  private void testChords(Accidental accidental, Scale scale, int numberOfNotesInChord) {
    for (int degree = 0; degree < scale.getNotes().size(); degree++) {
      try {
        Scale expectedScale = scale.getChord(degree, numberOfNotesInChord);
        String expectedName = expectedScale.asChord(accidental);
        Scale actualScale = Scales.parseChord(expectedName);
        String actualName = actualScale.asChord(accidental);
        System.out.println(expectedName);
        assertEquals(String.format("%s : %s", scale.getName(), expectedName), expectedScale, actualScale);
        assertEquals(String.format("%s : %s", scale.getName(), expectedName), expectedName, actualName);
      } catch (ParseChordException e) {
        fail(e.toString());
      }
    }
  }

}
