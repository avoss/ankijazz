package com.ankijazz.midi.song;

import java.util.List;
import java.util.Optional;

import com.ankijazz.Utils;
import com.ankijazz.Utils.Interpolator;
import com.ankijazz.midi.Part;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bar {
  @EqualsAndHashCode.Include
  private final List<Chord> chords;
  @EqualsAndHashCode.Include
  private final Optional<Part> melody;
  
  private int beatsPerBar;
  private Interpolator interpolator;
  private Bar next;

  public Bar(Part melody, List<Chord> chords) {
    this.melody = Optional.ofNullable(melody);
    this.chords = chords;
  }
  
  public Bar(List<Chord> chords) {
    this.melody = Optional.empty();
    this.chords = chords;
  }

  public static Bar of(Part melody, Chord ... chords) {
    return new Bar(melody, List.of(chords));
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
