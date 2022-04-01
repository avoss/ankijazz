package de.jlab.scales.jtg;

import java.util.List;
import java.util.stream.Collectors;

import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Song;
import lombok.Getter;

/**
 * Wrapper for Song and Bar which removes equals/hashcode and adds indexOf(Bar) method to Song.
 */

@Getter
public class IdSong {

  private final Song song;
  private final List<IdBar> bars;

  @Getter
  public static class IdBar {
    private final Bar bar;

    public IdBar(Bar bar) {
      this.bar = bar;
    }
    public List<Chord> getChords() {
      return bar.getChords();
    }
  }

  public IdSong(Song song) {
    this.song = song;
    this.bars = song.getBars().stream().map(IdBar::new).collect(Collectors.toList());
  }

  public int indexOf(IdBar idBar) {
    return bars.indexOf(idBar);
  }

}
