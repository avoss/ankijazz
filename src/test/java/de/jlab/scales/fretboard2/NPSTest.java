package de.jlab.scales.fretboard2;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class NPSTest {

  @Test
  public void testCMajorCagedPositions() {
    List<Position> positions = NPS.C_MAJOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    Position p0 = positions.get(0);
    assertEquals("7 8 10|7 8 10|7 9 10|7 9 10|8 10|7 8 10", toString(p0));
    assertEquals(7, p0.getMinFret());
    assertEquals(10, p0.getMaxFret());
    
    assertEquals("10 12 13|10 12|9 10 12|9 10 12|10 12 13|10 12 13", toString(positions.get(1)));
    assertEquals("12 13 15|12 14 15|12 14 15|12 14|12 13 15|12 13 15", toString(positions.get(2)));
    assertEquals("3 5|2 3 5|2 3 5|2 4 5|3 5 6|3 5", toString(positions.get(3)));
    assertEquals("5 7 8|5 7 8|5 7|4 5 7|5 6 8|5 7 8", toString(positions.get(4)));
  }

  @Test
  public void testCMelodicMinorCagedPositions() {
    List<Position> positions = NPS.C_MELODIC_MINOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    assertEquals("5 7 8|5 6 8|5 7 9|5 7 8|6 8|5 7 8", toString(positions.get(0)));
  }

  @Test
  public void testCHarmonicMinorCagedPositions() {
    List<Position> positions = NPS.C_HARMONIC_MINOR_CAGED.getPositions();
    assertEquals(5, positions.size());
    assertEquals("10 11 13|10 11 14|10 12 13|10 12 13|12 13|10 11 13", toString(positions.get(0)));
  }

  @Test
  public void testMinor7Pentatonic() {
    List<Position> positions = NPS.C_MINOR7_PENTATONIC.getPositions();
    assertEquals(5, positions.size());
    assertEquals("8 11|8 10|8 10|8 10|8 11|8 11", toString(positions.get(0)));
    assertEquals("11 13|10 13|10 13|10 12|11 13|11 13", toString(positions.get(1)));
  }

  @Test
  public void testMinor6Pentatonic() {
    List<Position> positions = NPS.C_MINOR6_PENTATONIC.getPositions();
    assertEquals(5, positions.size());
    assertEquals("8 11|8 10|7 10|8 10|8 10|8 11", toString(positions.get(0)));
  }
  
  private String toString(Position position) {
    return IntStream.range(0, position.getTuning().getStrings().size()).mapToObj(stringIndex -> {
      return position.getFrets(stringIndex).stream().map(fret -> Integer.toString(fret)).collect(Collectors.joining(" "));
    }).collect(Collectors.joining("|"));
  }
}
