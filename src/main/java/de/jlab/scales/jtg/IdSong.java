package de.jlab.scales.jtg;

import java.util.List;
import java.util.stream.Collectors;

import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Song;
import lombok.Getter;

/**
 * removes equals/hashcode from song, bar, chord and provides indexOf methods
 */

@Getter
public class IdSong {

  private final Song song;
  private final List<IdBar> bars;
  private final List<IdChord> chords;

  @Getter
  public static class IdChord {
    private final Chord chord;

    public IdChord(Chord chord) {
      this.chord = chord;
    }
  }

  @Getter
  public static class IdBar {
    private final Bar bar;
    private final List<IdChord> chords;

    public IdBar(Bar bar) {
      this.bar = bar;
      chords = bar.getChords().stream().map(IdChord::new).collect(Collectors.toList());
    }
  }

  public IdSong(Song song) {
    this.song = song;
    this.bars = song.getBars().stream().map(IdBar::new).collect(Collectors.toList());
    this.chords = bars.stream().flatMap(bar -> bar.getChords().stream()).collect(Collectors.toList());
  }

  public int indexOf(IdBar idBar) {
    return bars.indexOf(idBar);
  }

  public int indexOf(IdChord idChord) {
    return chords.indexOf(idChord);
  }

}
