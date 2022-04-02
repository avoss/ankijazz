package com.ankijazz.fretboard;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.ankijazz.fretboard.Fingering;
import com.ankijazz.fretboard.NPS;
import com.ankijazz.fretboard.Position;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scales;

public class NPSTest {

  @Test
  public void testCMajorCagedPositions() {
    List<Position> positions = NPS.C_MAJOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    Position p0 = positions.get(0);
    assertEquals("7 8 10|7 8 10|7 9 10|7 9 10|8 10|7 8 10", p0.toString());
    assertEquals(7, p0.getMinFret());
    assertEquals(10, p0.getMaxFret());
    
    assertEquals("10 12 13|10 12|9 10 12|9 10 12|10 12 13|10 12 13", positions.get(1).toString());
    assertEquals("12 13 15|12 14 15|12 14 15|12 14|12 13 15|12 13 15", positions.get(2).toString());
    assertEquals("3 5|2 3 5|2 3 5|2 4 5|3 5 6|3 5", positions.get(3).toString());
    assertEquals("5 7 8|5 7 8|5 7|4 5 7|5 6 8|5 7 8", positions.get(4).toString());
  }

  @Test
  public void testCMelodicMinorCagedPositions() {
    List<Position> positions = NPS.C_MELODIC_MINOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    assertEquals("5 7 8|5 6 8|5 7 9|5 7 8|6 8|5 7 8", positions.get(0).toString());
  }
  
  @Test
  public void testCMelodicMinorCagedException() {
    List<Position> positions = NPS.C_MELODIC_MINOR_CAGED.getPositions();
    assertEquals("7 8 10|6 8 10|7 9 10|7 8 10|8 10|7 8 10", positions.get(1).toString());
    assertEquals("11 13 15|12 14 15|12 13 15|12 14|12 13 15|11 13 15", positions.get(3).toString());
  }

  @Test
  public void testCHarmonicMinorCagedPositions() {
    List<Position> positions = NPS.C_HARMONIC_MINOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    assertEquals("10 11 13|10 11 14|10 12 13|10 12 13|12 13|10 11 13", positions.get(0).toString());
  }

  @Test
  public void testCHarmonicMinorCagedException() {
    List<Position> positions = NPS.C_HARMONIC_MINOR_CAGED.getPositions();
    assertEquals("8 10 11|8 10 11|9 10|7 8 10|8 9|7 8 10", positions.get(4).toString());
  }
  
  @Test
  public void testMinor7Pentatonic() {
    List<Position> positions = NPS.C_MINOR7_PENTATONIC.getPositions();
    assertEquals(5, positions.size());
    assertEquals("8 11|8 10|8 10|8 10|8 11|8 11", positions.get(0).toString());
    assertEquals("11 13|10 13|10 13|10 12|11 13|11 13", positions.get(1).toString());
  }

  @Test
  public void testMinor6Pentatonic() {
    List<Position> positions = NPS.C_MINOR6_PENTATONIC.getPositions();
    assertEquals(5, positions.size());
    assertEquals("8 11|8 10|7 10|8 10|8 10|8 11", positions.get(0).toString());
  }

  @Test
  public void testGetScale() {
    assertEquals(Scales.CHarmonicMinor, NPS.C_HARMONIC_MINOR_CAGED.getScale());
  }
  
  @Test
  public void testTransposePosition() {
    Fingering aMinorPentatonic = NPS.C_MINOR7_PENTATONIC.transpose(Note.A);
    assertEquals("5 8|5 7|5 7|5 7|5 8|5 8", aMinorPentatonic.getPositions().get(0).toString());
    assertEquals(Scales.CMinor7Pentatonic.transpose(-3), aMinorPentatonic.getScale());
    Fingering gMinorPentatonic = aMinorPentatonic.transpose(Note.G);
    Position position = gMinorPentatonic.getPositions().get(0);
    String positionString = "3 6|3 5|3 5|3 5|3 6|3 6";
    assertEquals(positionString, position.toString());
    assertEquals(positionString, position.transpose(12).toString());
    assertEquals(positionString, position.transpose(-12).toString());
    assertEquals(Scales.CMinor7Pentatonic.transpose(Note.G), gMinorPentatonic.getScale());
  }
  
}
