package de.jlab.scales.lily;

import static de.jlab.scales.lily.LilyUtil.toLilyNote;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import lombok.Data;

public class ChordToLilyMapper {
  
  @Data
  public static class LilyChord {
    private final String chord;
    private final String notes;
  }
  
  private static ScaleUniverse chordInfos = new ScaleUniverse(false, List.of(BuiltinChordType.values()));
  private static Map<String, String> map = new HashMap<>();
  static {
    map.put("Δ7", "maj7");
    map.put("Δ9", "maj9");
    map.put("5", "5");
    map.put("6", "6");
    map.put("69", "6.9");
    map.put("Δ7#11", "maj7.11+");
    map.put("m7", "m7");
    map.put("m6", "m6");
    map.put("m69", "m6.9");
    map.put("m9", "m9");
    map.put("m11", "m11");
    map.put("7", "7");
    map.put("9", "9");
    map.put("11", "11");
    map.put("13", "13");
    map.put("m7b5", "m7.5-");
    map.put("dim7", "dim7");
    map.put("mΔ7", "m7+");
    map.put("Δ7#5", "maj7.5+");
    map.put("7sus4", "7sus4");
    map.put("7b9", "7.9-");
    map.put("7#9", "7.9+");
    map.put("7b5", "7.5-");
    map.put("7#5", "7.5+");
    map.put("7#11", "7.11+");
    map.put("7b13", "7.13-");
    map.put("7b5b9", "7.5-9-");
    map.put("7#5b9", "7.5+9-");
    map.put("7b5#9", "7.5-9+");
    map.put("7#5#9", "7.5+9+");
    
    map.put("7b9#11", "7.9-11+");
    map.put("7b9b13", "7.9-13-");
    map.put("7#9#11", "7.9+11+");
    map.put("7#9b13", "7.9+13-");
    
    map.put("", "");
    map.put("m", "m");
    map.put("dim", "dim");
    map.put("aug", "aug");
    map.put("sus4", "sus4");
  }
  
  public LilyChord chordToLily(KeySignature keySignature, Scale chord, int denominator) {
    ScaleInfo info = chordInfos.findFirstOrElseThrow(chord);
    String lilyType = map.get(info.getScaleType().getTypeName());
    if (lilyType == null) {
      throw new IllegalArgumentException("Unknown chord type: " + info.getScaleType().getTypeName());
    }
    String lilyRoot = toLilyNote(keySignature, chord.getRoot());
    String lilyChord = format("%s%d:%s", lilyRoot, denominator, lilyType);
    String noteList = chord.stream().map(note -> toLilyNote(keySignature, note)).collect(joining(" "));
    String lilyNotes = format("<%s>%d", noteList, denominator);
    return new LilyChord(lilyChord, lilyNotes);
  }

}
