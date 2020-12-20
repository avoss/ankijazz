package de.jlab.scales.midi.song;

import java.util.List;

import lombok.Getter;

@Getter
public class Bar {

  private List<Chord> chords;

  public Bar(List<Chord> chords) {
    this.chords = chords;
  }

  public static Bar of(Chord ... chords) {
    return new Bar(List.of(chords));
  }

}
