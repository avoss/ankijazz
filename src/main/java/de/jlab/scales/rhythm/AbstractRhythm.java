package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

public abstract class AbstractRhythm implements Comparable<AbstractRhythm> {
  
  private final double TIED_WEIGHT = 1.0;
  private List<EventSequence> sequences;
  private Set<EventSequence> tiedSequences;
  private int difficulty;

  
  protected AbstractRhythm(List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    this.sequences = sequences;
    this.tiedSequences = tiedSequences;
    this.difficulty = computeDifficulty();
  }

  public int computeDifficulty() {
    double difficulty = getUniqueSequences().stream().mapToDouble(q -> q.getDifficulty()).sum();
    difficulty = difficulty * (1.0 + TIED_WEIGHT * (double) tiedSequences.size() / (double) sequences.size());
    difficulty += 2 * getUniqueSequences().stream().map(q -> q.isTriplet()).count();
    return (int) difficulty;
  }
  
  public int getDifficulty() {
    return difficulty;
  }

  public Set<EventSequence> getUniqueSequences() {
    return sequences.stream().collect(toSet());
  }

  public List<EventSequence> getSequences() {
    return sequences;
  }
  
  public boolean isTied(EventSequence sequence) {
    return tiedSequences.contains(sequence);
  }
  
  public boolean hasTies() {
    return !tiedSequences.isEmpty();
  }

  @Override
  public int compareTo(AbstractRhythm other) {
    return Integer.compare(computeDifficulty(), other.computeDifficulty());
  }
  
  public abstract String getTitle();

  public abstract String getTypeName();
  
}
