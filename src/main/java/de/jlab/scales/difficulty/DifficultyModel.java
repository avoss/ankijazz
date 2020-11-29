package de.jlab.scales.difficulty;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@RequiredArgsConstructor
public class DifficultyModel implements WithDifficulty {
  
  public interface Factor extends WithDifficulty {
    double getWeight();
  }

  @Getter 
  @Setter 
  @AllArgsConstructor 
  @ToString 
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  @Builder
  public static class DoubleFactor implements Factor {
    @EqualsAndHashCode.Include
    private final double min;
    @EqualsAndHashCode.Include
    private final double max;
    @EqualsAndHashCode.Include
    private final double weight;
    private double difficulty;
    
    public DoubleFactor update(double value) {
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
  @Setter 
  @AllArgsConstructor 
  @ToString 
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  @Builder
  public static class BooleanFactor implements Factor {
    @EqualsAndHashCode.Include
    private final double weight;
    @EqualsAndHashCode.Include
    private final boolean difficultValue;
    private double difficulty;
    
    public BooleanFactor update(boolean value) {
      difficulty = (value == difficultValue) ? 1 : 0;
      return this;
    }
  }

  private final Set<Factor> factors = new HashSet<>();
  
  public double getDifficulty() {
    double totalWeight = factors.stream().mapToDouble(f -> f.getWeight()).sum();
    double totalValues = factors.stream().map(f -> f.getWeight() * f.getDifficulty()).mapToDouble(d -> d).sum();
    return totalValues / totalWeight;
  }

  public DifficultyModel register(Factor factor) {
    factors.add(factor);
    return this;
  }

}
