package de.jlab.scales.theory;

import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.ScaleParser.asScale;
import static org.junit.Assert.*;

import org.junit.Test;

public class ScaleParserTest {

  @Test
  public void testAsScale() {
    assertEquals("C E G Bb", asScale(C7));
    assertEquals("Gb Ab Bb Cb Db Eb F", asScale(CMajor.transpose(Gb)));
    assertEquals("Bb Cb Db Eb F Gb Ab", asScale(CMajor.transpose(Gb).superimpose(Bb)));

    assertEquals("C D E F G A B", asScale(CMajor));
    assertEquals("G A B C D E F#", asScale(CMajor.transpose(G)));
    assertEquals("F G A Bb C D E", asScale(CMajor.transpose(F)));
    
    assertEquals("E G# B", asScale(new Scale(E, Ab, B)));
    assertEquals("D F# A C", asScale(C7.transpose(D)));
    
    assertEquals("C Db D Eb E F Gb", asScale(new Scale(C, Db, D, Eb, E, F, Gb)));
  }
  
  @Test
  public void majorTriadsShouldUseAccidentalFromIonianScale() {
    String[] expected = {
        "C E G",
        "Db F Ab",
        "D F# A",
        "Eb G Bb",
        "E G# B",
        "F A C",
        "Gb Bb Db", //"F# A# C#",
        "G B D",
        "Ab C Eb",
        "A C# E",
        "Bb D F",
        "B D# F#"
    };
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], asScale(CmajTriad.transpose(i)));
    }
  }

  @Test
  public void minorTriadsShouldUseAccidentalFromAeoleanScale() {
    String[] expected = {
        "C Eb G",
        "C# E G#",
        "D F A",
        "Eb Gb Bb",
        "E G B",
        "F Ab C",
        "F# A C#",
        "G Bb D",
        "G# B D#",
        "A C E",
        "Bb Db F", // "A# C# F",
        "B D F#"
    };
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], asScale(CminTriad.transpose(i)));
    }
  }

}
