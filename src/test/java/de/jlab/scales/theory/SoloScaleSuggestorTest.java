package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static de.jlab.scales.theory.ScaleUniverse.SCALES;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
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

import de.jlab.scales.theory.SoloScaleSuggestor.DefaultStrategy;
import de.jlab.scales.theory.SoloScaleSuggestor.Vertex;

public class SoloScaleSuggestorTest {

  private static final ScaleUniverse UNIVERSE = new ScaleUniverse(false, 
      List.of(BuiltinScaleType.Major, BuiltinScaleType.MelodicMinor, BuiltinScaleType.HarmonicMinor, BuiltinScaleType.DiminishedHalfWhole)
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
    Vertex cmaj = new Vertex(SCALES.findFirstOrElseThrow(CMajor));
    Vertex gmaj = new Vertex(SCALES.findFirstOrElseThrow(CMajor.transpose(G)));
    Vertex cmm = new Vertex(SCALES.findFirstOrElseThrow(CMelodicMinor));
    assertThat(strategy.weight(cmaj, cmm)).isGreaterThan(strategy.weight(cmaj, gmaj));
  }

  @Test
  public void testVertex() {
    Vertex v1 = new SoloScaleSuggestor.Vertex(null);
    Vertex v2 = new SoloScaleSuggestor.Vertex(null);
    assertThat(v1).isNotEqualTo(v2);
  }
  
  @Test
  public void testPaths() {
    assertThat(computePaths(Scales.C7).toString()).isEqualTo("[F Major Scale, F Melodic Minor, G Melodic Minor, F Harmonic Minor, C Diminished Half/Whole]");
    assertThatThrownBy(() -> computePaths(new Scale(C, Db, D, Eb, E))).isInstanceOf(SoloScaleSuggestor.ScaleNotFoundException.class);
  }
  
  @Test
  public void printSpainSolo() {
    // Spain Solo //
    List<Scale> chords = List.of(
      Scales.Cmaj7.transpose(G),      //GΔ7
      Scales.C7.transpose(Gb),        //F#7
      Scales.Cm7.transpose(E),        //Em7
      Scales.C7.transpose(A),         //A7
      Scales.Cmaj7.transpose(D),      //DΔ7
      Scales.Cmaj7.transpose(G),      //GΔ7
      Scales.C7flat5.transpose(Db),   //C#7b5
      Scales.C7sharp5sharp9.transpose(Gb),  //F#7#9
      Scales.C7sus4.transpose(B),     //B7sus4
      Scales.C7sharp5.transpose(B)    //B7#5 
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
    for (List<ScaleInfo> path : suggestor) {
      assertThat(path.size()).isEqualTo(chords.size());
      paths.add(toString(path));
    }
    return paths;
  }

  private String toString(List<ScaleInfo> path) {
    return path.stream().map(ScaleInfo::getScaleName).collect(Collectors.joining(", "));
  }

}
