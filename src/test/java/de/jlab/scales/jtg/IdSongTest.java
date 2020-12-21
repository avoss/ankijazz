package de.jlab.scales.jtg;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import de.jlab.scales.jtg.IdSong.IdBar;
import de.jlab.scales.jtg.IdSong.IdChord;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.Scales;

public class IdSongTest {

  @Test
  public void testIndexOf() {
    Chord chord = Chord.of(Scales.Cm7, "Cm7");
    Bar bar = Bar.of(chord, chord);
    Song song = Song.of(bar, bar);
    IdSong idSong = new IdSong(song);
    IdBar b0 = idSong.getBars().get(0);
    assertEquals(0, idSong.indexOf(b0));
    IdBar b1 = idSong.getBars().get(1);
    assertNotEquals(b0, b1);
    assertEquals(1, idSong.indexOf(b1));
    IdChord c0 = b0.getChords().get(0);
    assertEquals(0, idSong.indexOf(c0));
    IdChord c3 = b1.getChords().get(1);
    assertNotEquals(c0, c3);
    assertEquals(3, idSong.indexOf(c3));
  }
  
  @Test
  public void testGetChord() {
    List<Bar> bars = IntStream.range(0, 12).mapToObj(i->Bar.of(Chord.of(Scales.C7.transpose(i), "x"))).collect(toUnmodifiableList());
    Song song = new Song(bars);
    IdSong idSong = new IdSong(song);
    Chord chord = idSong.getChords().get(7).getChord();
    assertEquals(Scales.C7.transpose(7), chord.getScale());

  }

}
