package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.MidiTestUtils.song;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.Note;

public class SongDifficultyModelTest {

  final SongDifficultyModel model = new SongDifficultyModel();

  @Test
  public void testAllChordsCanBeHandled() {
    assertThat(model.getChordDifficulty(CmajTriad.transpose(Note.Eb))).isLessThan(model.getChordDifficulty(Cmaj7));
    BuiltinChordType.stream().forEach(type -> model.getChordDifficulty(type.getPrototype()));
  }

  @Test
  public void testNumberOfDifferentChordsMatter() {
    assertThat(model.getDifficulty(song("Cm7 Cm7"))).isLessThan(model.getDifficulty(song("Cm7 Dm7")));
  }

  @Test
  public void testNumberOfAccidentalsMatter() {
    assertThat(model.getDifficulty(song("Am7 Am7"))).isLessThan(model.getDifficulty(song("Bbm7 Bbm7")));
    assertThat(model.getDifficulty(song("Am7 Am7"))).isLessThan(model.getDifficulty(song("Bm7 Bm7")));
  }

}
