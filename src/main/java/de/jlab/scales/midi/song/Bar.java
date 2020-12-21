package de.jlab.scales.midi.song;

import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import lombok.Getter;

@Getter
public class Bar {

  private List<Chord> chords;
  private Interpolator interpolator;
  private int beatsPerBar;
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
