package de.jlab.scales.difficulty;

import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@RequiredArgsConstructor
@ToString
public class DifficultyModel implements WithDifficulty {
  
  public interface Term extends WithDifficulty {
    double getWeight();
  }

  @Getter 
  @RequiredArgsConstructor 
  @ToString 
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  public static class DoubleTerm implements Term {
    @EqualsAndHashCode.Include
    private final double min;
    @EqualsAndHashCode.Include
    private final double max;
    @EqualsAndHashCode.Include
    private final double weight;
    private double difficulty;
    
    public DoubleTerm update(double value) {
      double clipped = clip(value);
      this.difficulty = (clipped - min) / (max - min);
      return this;
    }

    private double clip(double value) {
      if (value < min) {
        return min;
      }
      if (value > max) {
        return max;
      }
      return value;
    }
  }

  @Getter 
  @RequiredArgsConstructor 
  @ToString 
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  public static class BooleanTerm implements Term {
    @EqualsAndHashCode.Include
    private final double weight;
    private double difficulty;
    
    public BooleanTerm update(boolean value) {
      difficulty = value ? 1 : 0;
      return this;
    }
  }

  private final Set<Term> terms = new HashSet<>();
  
  @Override
  public double getDifficulty() {
    double totalWeight = terms.stream().mapToDouble(f -> f.getWeight()).sum();
    double totalValues = terms.stream().map(f -> f.getWeight() * f.getDifficulty()).mapToDouble(d -> d).sum();
    double difficulty = totalValues / totalWeight;
    if (difficulty > 1.0) {
      throw new IllegalStateException("cannot have difficulty > 1.0: " + toString());
    }
    return difficulty;
  }

  public DoubleTerm doubleTerm(double min, double max, double weight) {
    DoubleTerm factor = new DoubleTerm(min, max, weight);
    register(factor);
    return factor;
  }
  
  public BooleanTerm booleanTerm(double weight) {
    BooleanTerm factor = new BooleanTerm(weight);
    register(factor);
    return factor;
  }
  
  public DifficultyModel register(Term term) {
    terms.add(term);
    return this;
  }

}
