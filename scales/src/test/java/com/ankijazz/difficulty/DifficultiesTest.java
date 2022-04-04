package com.ankijazz.difficulty;

import static com.ankijazz.difficulty.Difficulties.getChordDifficulty;
import static com.ankijazz.difficulty.Difficulties.getChordTypeDifficulty;
import static com.ankijazz.difficulty.Difficulties.getSongDifficulty;
import static com.ankijazz.midi.song.MidiTestUtils.song;
import static com.ankijazz.theory.Scales.Cmaj7;
import static com.ankijazz.theory.Scales.CmajTriad;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.ankijazz.theory.BuiltinChordType;
import com.ankijazz.theory.Note;

public class DifficultiesTest {

  @Test
  public void testChordDifficulty() {
    assertThat(getChordDifficulty(CmajTriad)).isLessThan(getChordDifficulty(Cmaj7));
    assertThat(getChordDifficulty(Cmaj7)).isLessThan(getChordDifficulty(Cmaj7.transpose(Note.Bb)));
  }
  
  @Test
  public void testAllChordsCanBeHandled() {
    assertThat(getChordTypeDifficulty(CmajTriad.transpose(Note.Eb))).isLessThan(getChordTypeDifficulty(Cmaj7));
    BuiltinChordType.stream().forEach(type -> getChordTypeDifficulty(type.getPrototype()));
  }

  @Test
  public void testNumberOfDifferentChordsMatter() {
    assertThat(getSongDifficulty(song("Cm7 Cm7"))).isLessThan(getSongDifficulty(song("Cm7 Dm7")));
  }

  @Test
  public void testNumberOfAccidentalsMatter() {
    assertThat(getSongDifficulty(song("Am7 Am7"))).isLessThan(getSongDifficulty(song("Bbm7 Bbm7")));
    assertThat(getSongDifficulty(song("Am7 Am7"))).isLessThan(getSongDifficulty(song("Bm7 Bm7")));
  }

}
