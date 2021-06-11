package de.jlab.scales.theory;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.Utils;

public class ChrisBollinger {
  
  @Test
  public void findScalesContainingChord() {
    Scale chord = Scales.Cm7b5.transpose(Note.Ab);
    //Scale chord = Scales.CaugTriad.transpose(Note.F);
    System.out.println(chord);
    for (ScaleInfo info : ScaleUniverse.SCALES.findScalesContaining(chord.asSet())) {
      System.out.println(format("%20s = %s", info.getScaleName(), info.getKeySignature().toString(info.getScale())  ));
    }
  }
  
  Collection<? extends Scale> chordsToSpell() {
    List<Scale> chords = new ArrayList<>();
    Iterator<Note> roots = Utils.loopIterator(Arrays.asList(Note.values()));
      for (Scale chord : Scales.allChords()) {
        for (int i = 0; i < 7; i++) {
          chords.add(chord.transpose(roots.next()));
        }
      }
    return chords;
  }
  
  @Test
  public void spellChords() throws IOException {
    List<String> lines = new ArrayList<>();
    for (Scale chord : chordsToSpell()) {
      for (ScaleInfo chordInfo : ScaleUniverse.CHORDS.infos(chord)) {
        String label = chord.getNumberOfNotes() == 3 ? "Triad" : "Chord";
        String name = chordInfo.getScaleName();
        String notes = chordInfo.getKeySignature().toString(chordInfo.getScale().stackedThirds());
        lines.add(format("%s;%s;%s", label, name, notes));
      }
    }
    Files.write(Paths.get("build", "Chords.csv"), lines);
  }

  @Test
  public void mappings() throws IOException {
    List<String> lines = new ArrayList<>();
    for (BuiltinChordType type : BuiltinChordType.values()) {
      String scale = format("%s %s", type.getSignatureScaleRoot(), type.getSignatureScaleType().getTypeName());
      String chord = format("%s%s", type.getPrototype().getRoot(), type.getTypeName());
      lines.add(format("%8s;%s", chord, scale));
    }
    Files.write(Paths.get("build", "Mapping.csv"), lines);
  }
  
  @Test
  public void scales() throws IOException {
    List<String> lines = new ArrayList<>();
    ScaleType[] types = new ScaleType[] {BuiltinScaleType.Major, BuiltinScaleType.HarmonicMinor, BuiltinScaleType.MelodicMinor};
    for (ScaleType type : types) {
      for (Note root : Note.values()) {
        Scale scale = type.getPrototype().transpose(root);
        for (KeySignature signature : type.getKeySignatures(root)) {
          String name = format("%s %s", signature.getKeySignatureString(), type.getTypeName());
          String notes = signature.toString(scale);
          lines.add(format("%s;%s", name, notes));
        }
      }
    }
    Files.write(Paths.get("build", "Scales.csv"), lines);
  }
  
  @Test 
  public void chrisBollinger() {
    StringBuilder sb = new StringBuilder();
    sb.append("Mapping\n   Chord -> Scale to use for notation\n");
    for (BuiltinChordType type : BuiltinChordType.values()) {
      String scale = format("%s %s", type.getSignatureScaleRoot(), type.getSignatureScaleType().getTypeName());
      String chord = format("%s%s", type.getPrototype().getRoot(), type.getTypeName());
      sb.append(format("%8s -> %s\n", chord, scale));
    }
    sb.append("\n");
    ScaleType[] types = new ScaleType[] {BuiltinScaleType.Major, BuiltinScaleType.HarmonicMinor, BuiltinScaleType.MelodicMinor};
    for (ScaleType type : types) {
      sb.append(format("\nScale: %s\n", type.getTypeName()));
      for (Note root : Note.values()) {
        Scale scale = type.getPrototype().transpose(root);
        for (KeySignature signature : type.getKeySignatures(root)) {
          String name = format("%s %s", signature.getKeySignatureString(), type.getTypeName());
          String notes = signature.toString(scale);
          sb.append(format("%s = %s\n", name, notes));
        }
      }
    }
    System.out.println(sb.toString());
  }

}
