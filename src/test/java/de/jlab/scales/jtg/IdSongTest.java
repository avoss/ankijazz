package de.jlab.scales.jtg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.jtg.IdSong.IdBar;
import de.jlab.scales.jtg.IdSong.IdChord;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.Scales;

public class IdSongTest {

  @Test
  public void test() {
    Chord chord = Chord.of(Scales.Cm7, "Cm7");
    Bar bar = Bar.of(chord, chord);
    Song song = Song.of(bar, bar);
    IdSong idSong = new IdSong(song);
    IdBar b0 = idSong.getBars().get(0);
    assertEquals(0, idSong.indexOf(b0));
    IdBar b1 = idSong.getBars().get(1);
    assertEquals(1, idSong.indexOf(b1));
    IdChord c0 = b0.getChords().get(0);
    assertEquals(0, idSong.indexOf(c0));
    IdChord c3 = b1.getChords().get(1);
    assertEquals(3, idSong.indexOf(c3));
  }

}
