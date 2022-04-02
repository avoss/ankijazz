package com.ankijazz.sheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.ankijazz.midi.song.Bar;
import com.ankijazz.midi.song.Chord;
import com.ankijazz.midi.song.Song;
import com.ankijazz.sheet.IdSong;
import com.ankijazz.sheet.IdSong.IdBar;
import com.ankijazz.theory.Scales;

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
  }
  
}
