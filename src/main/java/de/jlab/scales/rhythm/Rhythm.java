package de.jlab.scales.rhythm;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

public class Rhythm implements Comparable<Rhythm> {
  private final double TIED_WEIGHT = 1.0;

  private List<EventSequence> sequences;
  private Set<EventSequence> tiedSequences;
  private String title;

  public Rhythm(List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    this(null, sequences, tiedSequences);
  }
  
  public Rhythm(String title, List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    this.title = title;
    this.sequences = sequences;
    this.tiedSequences = tiedSequences;
  }

  public int getDifficulty() {
    double difficulty = getUniqueSequences().stream().mapToDouble(q -> q.getDifficulty()).sum();
    difficulty = difficulty * (1.0 + TIED_WEIGHT * (double) tiedSequences.size() / (double) sequences.size());
    return (int) difficulty;
  }

  public Set<EventSequence> getUniqueSequences() {
    return sequences.stream().collect(toSet());
  }

  @Override
  public int compareTo(Rhythm other) {
    return Integer.compare(getDifficulty(), other.getDifficulty());
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
  
  public String getTitle() {
    return title;
  }

  public static Rhythm of(String title, List<EventSequence> sequences, Set<EventSequence> tiedSequences) {
    return new Rhythm(title, sequences, tiedSequences);
  }
}
