package com.ankijazz.theory;

import static com.ankijazz.theory.ChordParser.parseChords;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static com.ankijazz.theory.SmoothLineSuggestor.Mode.ASCENDING;
import static com.ankijazz.theory.SmoothLineSuggestor.Mode.DESCENDING;
import static com.ankijazz.theory.SmoothLineSuggestor.Mode.STEADY;
import static com.ankijazz.theory.SmoothLineSuggestor.Mode.STRICT_ASCENDING;
import static com.ankijazz.theory.SmoothLineSuggestor.Mode.STRICT_DESCENDING;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ankijazz.theory.SmoothLineSuggestor.Mode;

public class SmoothLineSuggestorTest {

  SmoothLineSuggestor suggestor = new SmoothLineSuggestor();
  
  @Test
  public void testAscending() {
    List<Scale> chords = parseChords("Cm C");
    assertLines(STRICT_ASCENDING, chords, List.of(List.of(Eb, E), List.of(C, E), List.of(G, C)));
    assertLines(ASCENDING, chords, List.of(List.of(C, C), List.of(G, G), List.of(Eb, E)));
  }

  @Test
  public void testDescending() {
    List<Scale> chords = parseChords("C Cm");
    assertLines(STRICT_DESCENDING, chords, List.of(List.of(E, Eb), List.of(G, Eb), List.of(C, G)));
    assertLines(DESCENDING, chords, List.of(List.of(C, C), List.of(G, G), List.of(E, Eb)));
  }
  
  @Test
  public void testSteady() {
    List<Scale> chords = parseChords("C Cm D");
    assertLines(STEADY, chords, List.of(List.of(G, G, Gb), List.of(C, C, D), List.of(E, Eb, D)));
  }
  
  
  private void assertLines(Mode mode, List<Scale> chords, List<List<Note>> expectedLines) {
    List<List<Note>> actualLines = suggestor.suggest(mode, chords).stream().map(line -> line.getNotes()).collect(Collectors.toList());
    assertThat(actualLines).isEqualTo(expectedLines);
  }
  
  @Test
  public void printHowHighTheMoon() {
    List<Scale> chords = parseChords("Em7 Eb7 Dm7 G7 Cmaj7 Ebmaj7 Abmaj7 Dbmaj7");
    printLines(DESCENDING, chords);
    printLines(ASCENDING, chords);
    printLines(STEADY, chords);
    
  }

  private void printLines(Mode mode, List<Scale> chords) {
    suggestor.suggest(mode, chords).stream().forEach(line -> {
      System.out.println(String.format("%10s %d %s", line.getMode(), line.getNumberOfSemitones(), line.getNotes()));
    });
      
  }

}
