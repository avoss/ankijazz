package de.jlab.scales.theory;

import static java.lang.String.format;

import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

// todo parts contain bars contain chords
public class Song {

  public class Chord {
    private List<Scale> validScales = Lists.newArrayList();
    private Scale chord;
    public Chord(Scale chord) {
      this.chord = chord;
      computeValidScales();
    }
    private void computeValidScales() {
      for (Scale scale : universe.getAllScales()) {
        if (scale.contains(chord)) 
          validScales.add(scale);
      }
    }
    
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(format("valid scales for chord %s:\n", chord.asChord()));
      for (Scale scale : validScales) {
        sb.append(format(" %s\n", scale.getName()));
        for (Scale sub : scale.getSubScales())
          sb.append(format("   >%s\n", sub.getName()));
      }
      return sb.toString();
    }
  }
  
  private ScaleUniverse universe;
  private List<Chord> chords = Lists.newArrayList();

  public Song(ScaleUniverse universe) {
    this.universe = universe;
  }

  public void parseChords(String songString) throws ParseChordException {
    for (String chordString : Splitter.on(CharMatcher.WHITESPACE).split(songString)) {
      chords.add(new Chord(Scales.parseChord(chordString)));
    }
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Chord c : chords)
      sb.append(format("%s\n", c));
    return sb.toString();
  }

}
