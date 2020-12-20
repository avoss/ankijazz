package de.jlab.scales.lily;

import java.util.HashMap;
import java.util.Map;

import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.ScaleType;

public class LilyJam {
  private final Map<String, String> map = new HashMap<>();
  private final Song song;

  public LilyJam(Song song) {
    this.song = song;
  }
  
  public String toLily() {
    return LilyUtil.readTemplate(LilyJam.class, "LilyJam.ly")
        .replace("${chords}", getLilyChordString());
  }

  private CharSequence getLilyChordString() {
    StringBuilder sb = new StringBuilder();
    for (ScaleType type : BuiltinChordType.values()) {
      sb.append("c1:".concat(map.get(type.getTypeName())));
    }
    return sb;
  }
  

}
