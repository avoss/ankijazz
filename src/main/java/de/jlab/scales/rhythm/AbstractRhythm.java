package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;

import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.difficulty.WithDifficulty;

public abstract class AbstractRhythm implements Comparable<AbstractRhythm>, WithDifficulty {
  
  private List<Quarter> quarters;
  private double difficulty;

  
  protected AbstractRhythm(List<Quarter> quarters) {
    this.quarters = quarters;
    this.difficulty = computeDifficulty();
  }

  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    model.doubleFactor(0, quarters.size(), 300).update(getUniqueQuarters().stream().count());
    model.doubleFactor(0, quarters.size(), 100).update(getUniqueQuarters().stream().mapToDouble(q -> q.getDifficulty()).sum());
    model.booleanFactor(5).update(getUniqueQuarters().stream().count() % 2 != 0);
    return model.getDifficulty();
  }
  
  @Override
  public double getDifficulty() {
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
    return Double.compare(getDifficulty(), other.getDifficulty());
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
