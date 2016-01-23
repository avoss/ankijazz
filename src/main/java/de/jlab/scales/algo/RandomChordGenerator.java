package de.jlab.scales.algo;

import java.util.Random;

import de.jlab.scales.random.AbstractSequence;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

/**
 * creates uniform distributed chords with no relationship among each other
 */

public class RandomChordGenerator extends AbstractSequence<Scale> {
  private Note[] roots = Note.values();
  private String[] qualities = { "m7", "m7b5", "7", "7b9", "Maj7", "Sus47", "7#5",  "7b5", "6", "m6", "79", "m79", "Maj79", "mMaj7", "Maj7#5"};
  private Random random = new Random();
  
  public RandomChordGenerator() {
    super(1);
  }
  
  public RandomChordGenerator qualities(String ... qualities) {
    this.qualities = qualities;
    return this;
  }

  public RandomChordGenerator roots(Note ... roots) {
    this.roots = roots;
    return this;
  }
  
  @Override
  public Scale next() {
    Note root = choose(roots);
    String quality = choose(qualities);
    return Scales.parseChord(root + quality);    
  }

  private <T> T choose(T[] array) {
    return array[random.nextInt(array.length)];
  }

}
