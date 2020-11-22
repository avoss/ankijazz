package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;

public abstract class AbstractRhythm implements Comparable<AbstractRhythm> {
  
  private List<Quarter> quarters;
  private int difficulty;

  
  protected AbstractRhythm(List<Quarter> quarters) {
    this.quarters = quarters;
    this.difficulty = computeDifficulty();
  }

  private int computeDifficulty() {
    double difficulty = getUniqueQuarters().stream().mapToDouble(q -> q.getDifficulty() * (q.isTied() ? 2 : 1)).sum();
    difficulty *= getUniqueQuarters().stream().count() % 2 == 0 ? 1.0 : 2.0;
    difficulty *= 1 +  getUniqueQuarters().stream().map(q -> q.isTriplet()).count();
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
  
  public int getLength() {
    return quarters.stream().map(Quarter::getLength).collect(Collectors.reducing(Fraction.ZERO, (a,b) -> a.add(b))).intValue();
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

  public <T extends AbstractRhythm> T transpose(int distance, Function<List<Quarter>, T> factory) {
    List<Quarter> transposed = new RhythmTransformer().transpose(getQuarters(), distance);
    return factory.apply(transposed);
  }


  @Override
  public String toString() {
    return quarters.stream().map(s -> s.toString()).collect(joining(" "));
  }
  
}
