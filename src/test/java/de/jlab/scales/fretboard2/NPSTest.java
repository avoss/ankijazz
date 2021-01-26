package de.jlab.scales.fretboard2;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class NPSTest {

  @Test
  public void testMajorScalePositions() {
    List<Position> positions = NPS.CMajor.getPositions();
    assertEquals(5, positions.size());
    assertEquals("7 8 10|7 8 10|7 9 10|7 9 10|8 10|7 8 10", toString(positions.get(0)));
    assertEquals("12 13 15|12 14 15|12 14 15|12 14|12 13 15|12 13 15", toString(positions.get(1)));
    assertEquals("5 7 8|5 7 8|5 7|4 5 7|5 6 8|5 7 8", toString(positions.get(2)));
    assertEquals("10 12 13|10 12|9 10 12|9 10 12|10 12 13|10 12 13", toString(positions.get(3)));
    assertEquals("3 5|2 3 5|2 3 5|2 4 5|3 5 6|3 5", toString(positions.get(4)));
  }

  private String toString(Position position) {
    return IntStream.range(0, position.getTuning().getStrings().size()).mapToObj(stringIndex -> {
      return position.getFrets(stringIndex).stream().map(fret -> Integer.toString(fret)).collect(Collectors.joining(" "));
    }).collect(Collectors.joining("|"));
  }
}
