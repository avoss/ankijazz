package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractRhythm implements Comparable<AbstractRhythm> {
  
  private List<EventSequence> sequences;
  private int difficulty;

  
  protected AbstractRhythm(List<EventSequence> sequences) {
    this.sequences = sequences;
    this.difficulty = computeDifficulty();
  }

  public int computeDifficulty() {
    double difficulty = getUniqueSequences().stream().mapToDouble(q -> q.getDifficulty() * (q.isTied() ? 2 : 1)).sum();
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
  
  public boolean hasTies() {
    return EventSequence.hasTies(sequences);
  }

  @Override
  public int compareTo(AbstractRhythm other) {
    return Integer.compare(computeDifficulty(), other.computeDifficulty());
  }
  
  public abstract String getTitle();

  public abstract String getTypeName();

  public <T extends AbstractRhythm> T transpose(Function<List<EventSequence>, T> factory) {
    
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String toString() {
    return sequences.stream().map(s -> s.toString()).collect(joining(" "));
  }
  
}
