package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractRhythm implements Comparable<AbstractRhythm> {
  
  private List<Quarter> quarters;
  private int difficulty;

  
  protected AbstractRhythm(List<Quarter> quarters) {
    this.quarters = quarters;
    this.difficulty = computeDifficulty();
  }

  public int computeDifficulty() {
    double difficulty = getUniqueQuarters().stream().mapToDouble(q -> q.getDifficulty() * (q.isTied() ? 2 : 1)).sum();
    difficulty += 2 * getUniqueQuarters().stream().map(q -> q.isTriplet()).count();
    return (int) difficulty;
  }
  
  public int getDifficulty() {
    return difficulty;
  }

  public Set<Quarter> getUniqueQuarters() {
    return quarters.stream().collect(toSet());
  }

  public List<Quarter> getQuarters() {
    return quarters;
  }
  
  public boolean hasTies() {
    return Quarter.hasTies(quarters);
  }

  @Override
  public int compareTo(AbstractRhythm other) {
    return Integer.compare(computeDifficulty(), other.computeDifficulty());
  }
  
  public abstract String getTitle();

  public abstract String getTypeName();

  public <T extends AbstractRhythm> T transpose(Function<List<Quarter>, T> factory) {
    
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String toString() {
    return quarters.stream().map(s -> s.toString()).collect(joining(" "));
  }
  
}
