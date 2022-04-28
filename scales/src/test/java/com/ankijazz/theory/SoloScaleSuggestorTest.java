package com.ankijazz.theory;

import static com.ankijazz.theory.Note.A;
import static com.ankijazz.theory.Note.B;
import static com.ankijazz.theory.Note.*;
import static com.ankijazz.theory.Note.C;
import static com.ankijazz.theory.Note.D;
import static com.ankijazz.theory.Note.Db;
import static com.ankijazz.theory.Note.E;
import static com.ankijazz.theory.Note.Eb;
import static com.ankijazz.theory.Note.G;
import static com.ankijazz.theory.Note.Gb;
import static com.ankijazz.theory.ScaleUniverse.SCALES;
import static com.ankijazz.theory.Scales.*;
import static com.ankijazz.theory.Scales.CMelodicMinor;
import static com.ankijazz.theory.Scales.Cmaj7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    assertThat(computePaths(Scales.C7).toString()).isEqualTo("[C7:F Major Scale, C7:F Melodic Minor, C7:G Melodic Minor, C7:F Harmonic Minor]");
    assertThatThrownBy(() -> computePaths(new Scale(C, Db, D, Eb, E))).isInstanceOf(SoloScaleSuggestor.ScaleNotFoundException.class);
  }
  
  @Test
  public void printSpainSolo() {
    // Spain Solo //
    List<Scale> chords = List.of(
      Cmaj7.transpose(G),      //GΔ7
      C7.transpose(Gb),        //F#7
      Cm7.transpose(E),        //Em7
      C7.transpose(A),         //A7
      Cmaj7.transpose(D),      //DΔ7
      Cmaj7.transpose(G),      //GΔ7
      C7flat5.transpose(Db),   //C#7b5
      C7sharp5sharp9.transpose(Gb),  //F#7#9
      C7sus4.transpose(B),     //B7sus4
      C7sharp5.transpose(B)    //B7#5 
    );
    System.out.println(computePaths(chords).stream().collect(Collectors.joining("\n")));
  }


  @Test
  public void printWITTCL_PartA_Solo() {
    List<Scale> chords = List.of(
      Cm7b5.transpose(G),
      C7flat9,
      Scales.CminTriad.transpose(F),
      Scales.CminTriad.transpose(F),
      Cm7b5.transpose(D),
      C7flat9.transpose(G),
      Scales.C6,
      Scales.C6
    );
    System.out.println(computePaths(chords).stream().collect(Collectors.joining("\n")));
  }
  
  @Test
  public void testHeathrowSolo() {
    String song = "F9 Dm6 Bb " + 
               "F9 Dm6 Gb Gb7sus4 F9 Dm6 Bb " +
               "F9 Dm6 Cm7 Gbm " +
               "Fm Eb Fsus4 Eb6 Fsus4";
    printChordNotes(song);
    printScaleOptions(song);
  }

  private void printChordNotes(String song) {
    List<Scale> chords = parseSong(song);
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
    List<Scale> chords = parseSong(song);
    System.out.println(computePaths(chords).stream().collect(Collectors.joining("\n")));
  }

  private List<Scale> parseSong(String song) {
    List<Scale> chords = Pattern.compile("\\s+")
      .splitAsStream(song)
      .map(chord -> ChordParser.parseChord(chord))
      .collect(Collectors.toList());
    return chords;
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
    return path.stream().map(v -> String.format("%s:%s", v.getChord().asChord(Accidental.FLAT), v.getInfo().getScaleName())).collect(Collectors.joining(", "));
  }

}
