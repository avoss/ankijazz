package de.jlab.scales.midi.song;

import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Song {
  private final List<Bar> bars;
  private int beatsPerBar;

  public Song(List<Bar> bars) {
    this(4, bars);
  }

  public Song(int beatsPerBar, List<Bar> bars) {
    this.beatsPerBar = beatsPerBar;
    this.bars = bars;
    initialize();
  }

  private void initialize() {
    Bar prev = bars.get(bars.size() - 1);
    for (Bar next : bars) {
      prev.initialize(beatsPerBar, next);
      prev = next;
    }
  }

  public static Song of(Bar... bars) {
    return new Song(List.of(bars));
  }

  @Override
  public String toString() {
    return bars.stream().map(bar -> bar.getChords().stream().map(Chord::getSymbol).collect(Collectors.joining(" "))).collect(Collectors.joining(" | ", "| ", " |"));
  }
}
