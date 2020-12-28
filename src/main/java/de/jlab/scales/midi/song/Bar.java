package de.jlab.scales.midi.song;

import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bar {
  @EqualsAndHashCode.Include
  private List<Chord> chords;
  
  private int beatsPerBar;
  private Interpolator interpolator;
  private Bar next;

  public Bar(List<Chord> chords) {
    this.chords = chords;
  }

  public static Bar of(Chord ... chords) {
    return new Bar(List.of(chords));
  }

  public void initialize(int beatsPerBar, Bar next) {
    this.beatsPerBar = beatsPerBar;
    this.next = next;
    interpolator = Utils.interpolator(0, beatsPerBar, 0, chords.size());
  }

  public Chord getChordForBeat(int beat) {
    if (beat >= beatsPerBar) {
      return next.getChordForBeat(beat - beatsPerBar);
    }
    return chords.get(interpolator.apply(beat));
  }

}
