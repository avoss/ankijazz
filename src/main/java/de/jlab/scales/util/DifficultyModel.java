package de.jlab.scales.util;

import java.util.HashMap;
import java.util.Map;

@lombok.RequiredArgsConstructor
public class DifficultyModel {
  
  private interface Factor {
    double getWeight();
  }
  
  @lombok.Data
  @lombok.Builder
  public static class DoubleFactor implements Factor {
    private final double min;
    private final double max;
    private final double weight;
    private final DifficultyModel model;
    
    public DoubleFactor(double min, double max, double weight, DifficultyModel model) {
      this.min = min;
      this.max = max;
      this.weight = weight;
      this.model = model;
      model.register(this);
    }
    
    public int update(double value) {
      double clipped = clip(value);
      double difficulty = (clipped - min) / (max - min);
      return model.update(this, difficulty);
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

  @lombok.Data
  @lombok.Builder
  public static class BooleanFactor implements Factor {
    private final double weight;
    private final boolean difficultValue;
    private final DifficultyModel model;
    public int update(boolean value) {
      double difficulty = (value == difficultValue) ? 1 : 0;
      return model.update(this, difficulty);
    }
  }

  private final Map<Factor, Double> factors = new HashMap<>();
  private final int range;
  
  public int update(Factor factor, double difficulty) {
    factors.put(factor, difficulty);
    return computeDifficulty();
  }

  public int computeDifficulty() {
    double totalWeight = factors.keySet().stream().mapToDouble(f -> f.getWeight()).sum();
    double totalValues = factors.entrySet().stream().map(e -> e.getKey().getWeight() * e.getValue()).mapToDouble(d -> d).sum();
    return (int) (totalValues / totalWeight * range);
  }

  public void register(Factor factor) {
    factors.put(factor, 0.0);
  }

}
