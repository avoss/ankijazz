package de.jlab.scales.difficulty;

import static de.jlab.scales.difficulty.Difficulties.getChordDifficulty;
import static de.jlab.scales.difficulty.Difficulties.getChordTypeDifficulty;
import static de.jlab.scales.difficulty.Difficulties.getSongDifficulty;
import static de.jlab.scales.midi.song.MidiTestUtils.song;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.Test;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;

public class DifficultiesTest {
  private static final Offset<Double> EPS = Offset.offset(0.05);
  
  @Test
  public void testRefactoringDoesNotChangeValues() {
    assertThat(getSongDifficulty(song("Cm Dm A7 F"))).isCloseTo(0.30962, EPS);
    assertThat(getSongDifficulty(song("C9 EbΔ7#11 C69 G7"))).isCloseTo(0.51346, EPS);
    assertThat(getSongDifficulty(song("A9 D#7b5#9 C7#9b13 Db7#5#9"))).isCloseTo(0.75481, EPS);
  }
  
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
