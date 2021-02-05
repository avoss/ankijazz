package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.jlab.scales.Utils;
import de.jlab.scales.anki.OutlineChordsWithPentatonicsCardGenerator.ChordPentaPair;
import de.jlab.scales.theory.Note;

public class OutlineChordsWithPentatonicsCardGeneratorTest {

  @Test
  public void test() {
    OutlineChordsWithPentatonicsCardGenerator generator = new OutlineChordsWithPentatonicsCardGenerator(Utils.fixedLoopIteratorFactory());
    assertThat(generator.findPairs()).contains(new ChordPentaPair(Major7, Minor7Pentatonic.getPrototype().transpose(Note.E)));
  }

}
