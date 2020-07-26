package de.jlab.scales.theory;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class ChordParserTest1 {

  private String chordName;
  
  public ChordParserTest1(String chordName) {
    this.chordName = chordName;
  }


  @Parameters(name="Chord={0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] { 
        { "csus7" }, 
        { "Co" }, 
        { "Esus4" }, 
        { "Ebsus4" }, 
        { "EBSUS2" }, 
        { "F" }, 
        { "Fm7" }, 
        { "Fm" }, 
        { "F7" }, 
        { "c79" }, 
        });
  }


  @Test
  public void test() {
    assertEquals(chordName.toLowerCase(), Scales.parseChord(chordName).asChord().toLowerCase());
  }
}
