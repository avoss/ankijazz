package com.ankijazz.difficulty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DifficultyCollection {
  
  private final Comparator<WithDifficulty> COMPARATOR = (a,b) -> Double.compare(a.getDifficulty(), b.getDifficulty());
  private final ThreadLocalRandom random = ThreadLocalRandom.current();
  
  public static void sort(List<? extends WithDifficulty> list) {
    sort(list, 0.0);
  }
  
  public static <T extends WithDifficulty> void sort(List<T> list, double randomness) {
    new DifficultyCollection().internalSort(list, randomness);
  }

  private class RandomDifficulty<T extends WithDifficulty> implements WithDifficulty {
    private final T delegate;
    private final double difficulty;

    RandomDifficulty(T delegate, double difficulty) {
      this.delegate = delegate;
      this.difficulty = difficulty;
    }
    @Override
    public double getDifficulty() {
      return difficulty;
    }
    public T getDelegate() {
      return delegate;
    }
  }
  
  private <T extends WithDifficulty> void internalSort(List<T> list, double randomness) {
    if (randomness <= 0) {
      Collections.sort(list, COMPARATOR);
      return;
    }
    List<RandomDifficulty<T>> temp = new ArrayList<>();
    list.stream().map(t -> new RandomDifficulty<>(t, t.getDifficulty() + random.nextDouble(randomness))).forEach(temp::add);
    Collections.shuffle(temp);
    Collections.sort(temp, COMPARATOR);
    list.clear();
    temp.stream().map(r -> r.getDelegate()).forEach(list::add);
  }

}
