package com.ankijazz.theory;

import static com.ankijazz.theory.ChordParser.parseChords;
import static com.ankijazz.theory.Note.Bb;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.ScaleUniverse.SCALES;
import static com.ankijazz.theory.Scales.CMajor;
import static com.ankijazz.theory.Scales.CMelodicMinor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import com.ankijazz.theory.SoloScaleSuggestor.DefaultStrategy;
import com.ankijazz.theory.SoloScaleSuggestor.Vertex;

public class SoloScaleSuggestorTest {

  private static final ScaleUniverse UNIVERSE = new ScaleUniverse(false, 
      List.of(BuiltinScaleType.Major, BuiltinScaleType.MelodicMinor, BuiltinScaleType.HarmonicMinor)
  );

  @Test
  public void testNumberOfDifferentNotes() {
    assertThat(weight(CMajor, CMajor)).isEqualTo(0);
    assertThat(weight(new Scale(C), new Scale(C))).isEqualTo(0);
    assertThat(weight(new Scale(C), new Scale(C, D))).isEqualTo(1);
    assertThat(weight(new Scale(C, D), new Scale(C))).isEqualTo(1);
    assertThat(weight(new Scale(C), new Scale(D))).isEqualTo(2);
    assertThat(weight(CMajor, CMajor.transpose(G))).isEqualTo(2);
    assertThat(weight(CMajor, CMajor.transpose(Bb))).isEqualTo(4);
  }
  
  private double weight(Scale source, Scale target) {
    return new SoloScaleSuggestor.DefaultStrategy(UNIVERSE).numberOfDifferentNotes(source, target);
  }
  
  @Test
  public void testScaleDifficulty() {
    DefaultStrategy strategy = new SoloScaleSuggestor.DefaultStrategy(UNIVERSE);
    Vertex cmaj = new Vertex(null, SCALES.findFirstOrElseThrow(CMajor));
    Vertex gmaj = new Vertex(null, SCALES.findFirstOrElseThrow(CMajor.transpose(G)));
    Vertex cmm = new Vertex(null, SCALES.findFirstOrElseThrow(CMelodicMinor));
    assertThat(strategy.weight(cmaj, cmm)).isGreaterThan(strategy.weight(cmaj, gmaj));
  }

  @Test
  public void testVertex() {
    Vertex v1 = new SoloScaleSuggestor.Vertex(null, null);
    Vertex v2 = new SoloScaleSuggestor.Vertex(null, null);
    assertThat(v1).isNotEqualTo(v2);
  }
  
  @Test
  public void testPaths() {
    List<String> paths = computePaths(Scales.C7);
    assertThat(paths.size()).isEqualTo(4);
    assertThat(paths.toString()).contains("F Melodic Minor");
    assertThatThrownBy(() -> computePaths(new Scale(C, Db, D, Eb, E))).isInstanceOf(SoloScaleSuggestor.ScaleNotFoundException.class);
  }
  
  @Test
  @Ignore
  public void printStrangeCadence() {
    String song = "Dm7b5 G7b9 Em7 A7b9";
    printScaleOptions(song);
  }
  
  @Test
  @Ignore
  public void printHeathrowSolo() {
    String song = "F9 Dm6 Bb " + 
               "F9 Dm6 Gb Gb7sus4 F9 Dm6 Bb " +
               "F9 Dm6 Cm7 Gbm " +
               "Fm Eb Fsus4 Eb6 Fsus4";
    printChordNotes(song);
    printScaleOptions(song);
  }

  private void printChordNotes(String song) {
    List<Scale> chords = parseChords(song);
    Set<Scale> seen = new HashSet<>();
    for (Scale chord : chords) {
      if (seen.contains(chord)) {
        continue;
      }
      seen.add(chord);
      String notes = chord.stackedThirds().stream().map(note -> note.getName(Accidental.FLAT)).collect(Collectors.joining(" "));
      String name = chord.asChord(Accidental.FLAT);
      System.out.println(name + " = " + notes);
    }
  }

  private void printScaleOptions(String song) {
    List<Scale> chords = parseChords(song);
    System.out.println(computePaths(chords).stream().collect(Collectors.joining("\n\n")));
  }

  private List<String> computePaths(Scale ... chords) {
    return computePaths(Arrays.asList(chords));
  }
  
  private List<String> computePaths(List<Scale> chords) {
    SoloScaleSuggestor suggestor = new SoloScaleSuggestor(new SoloScaleSuggestor.DefaultStrategy(UNIVERSE), chords, 10);
    List<String> paths = new ArrayList<>();
    suggestor.stream().forEach( path -> {
      assertThat(path.size()).isEqualTo(chords.size());
      paths.add(toString(path));
    });
    return paths;
  }

  private String toString(List<Vertex> path) {
    return path.stream().map(v -> String.format("%8s : %s", v.getChord().asChord(Accidental.FLAT), v.getInfo().getScaleName())).collect(Collectors.joining("\n"));
  }

}
