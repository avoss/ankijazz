package de.jlab.scales.anki;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.Note;

import static de.jlab.scales.anki.OutlineChordsWithPentatonicsCardGenerator.ChordPentaPair.pair;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinScaleType.Minor7Pentatonic;
import static org.assertj.core.api.Assertions.*;

public class OutlineChordsWithPentatonicsCardGeneratorTest {

  @Test
  public void test() {
    OutlineChordsWithPentatonicsCardGenerator generator = new OutlineChordsWithPentatonicsCardGenerator(Utils.fixedLoopIteratorFactory());
    assertThat(generator.findPairs()).contains(pair(Major7, Minor7Pentatonic.getPrototype().transpose(Note.E)));
  }

}
