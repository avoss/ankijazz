package de.jlab.scales.rhythm;

import static de.jlab.scales.Utils.repeat;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.b3;
import static de.jlab.scales.rhythm.Event.r1;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Event.r4;
import static de.jlab.scales.rhythm.Quarter.q;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Optional;

import de.jlab.scales.Utils;

public class RhythmGenerator {

  private final Map<QuarterCategory, Collection<Quarter>> quarterMap;
  private final int numberOfRhythms = 300;
  private final int numberOfQuarters = 16;

  public RhythmGenerator() {
    this(new QuarterGenerator().getQuarterMap());
  }

  public RhythmGenerator(Map<QuarterCategory, Collection<Quarter>> quarterMap) {
    this.quarterMap = quarterMap;
  }
  
  static abstract class AbstractTies implements Function<List<Quarter>, Optional<RandomRhythm>> {

    @Override
    public Optional<RandomRhythm> apply(List<Quarter> quarters) {
      List<Quarter> copy = new ArrayList<>();
      for (int i = 1; i < quarters.size(); i++) {
        Quarter prev = quarters.get(i-1);
        Quarter next = quarters.get(i);
        copy.add(canCreateTie(prev, next) && shouldCreateTie(prev, next) ? prev.tie() : prev);
      }
      return result(quarters);
    }
    
    boolean canCreateTie(Quarter prev, Quarter next) {
      if (prev.getNumberOfEvents() + next.getNumberOfEvents() < 3) {
        return false;
      }
      return prev.endsWithBeat() && next.startsWithBeat();
    }
    
    protected abstract boolean shouldCreateTie(Quarter prev, Quarter next);
    protected abstract Optional<RandomRhythm> result(List<Quarter> quarters);
  }
  
  static class AllTies extends AbstractTies {

    @Override
    protected boolean shouldCreateTie(Quarter prev, Quarter next) {
      return true;
    }

    @Override
    protected Optional<RandomRhythm> result(List<Quarter> quarters) {
      if (Quarter.hasTies(quarters)) {
        return Optional.of(new RandomRhythm(quarters));
      }
      return Optional.absent();
    }
    
  }

  static class RandomTies extends AbstractTies {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    protected boolean shouldCreateTie(Quarter prev, Quarter next) {
      return random.nextBoolean(); 
    }

    @Override
    protected Optional<RandomRhythm> result(List<Quarter> quarters) {
      return Optional.of(new RandomRhythm(quarters));
    }
    
  }

  static class NoTies extends AbstractTies {

    @Override
    protected boolean shouldCreateTie(Quarter prev, Quarter next) {
      return false;
    }

    @Override
    protected Optional<RandomRhythm> result(List<Quarter> quarters) {
      return Optional.of(new RandomRhythm(quarters));
    }
    
  }
  
  public List<AbstractRhythm> generate() {
    List<AbstractRhythm> result = new ArrayList<>();
    result.addAll(basicRhythms(new NoTies()));
    result.addAll(basicRhythms(new AllTies()));
    result.addAll(standardRhythms());
//    result.addAll(groupingRhythms());
    result.addAll(randomRhythms(result.size()));
    return result;
  }


  private Collection<? extends AbstractRhythm> groupingRhythms() {
    List<AbstractRhythm> result = new ArrayList<>();
    Quarter q1 = q(b3, b1).tie();
    Quarter q2 = q(b2, b2).tie();
    Quarter q3 = q(b1, b3).tie();
    Quarter q4 = q(b3, b1);
    
    GroupingRhythm rhythm = new GroupingRhythm(0, 0, repeat(4, q1, q2, q3, q4));
    for (int i = 0; i < 3; i++) {
      result.add(rhythm);
      rhythm = rhythm.transpose();
    }
    return result;
  }

  private Collection<? extends AbstractRhythm> randomRhythms(int rhythmsSoFar) {
    List<AbstractRhythm> result = new ArrayList<>();
    RandomTies randomTies = new RandomTies();
    Iterator<QuarterCategory> categoryIterator = Utils.randomLoopIterator(quarterMap.keySet());
    int numberOfRandomRhythms = numberOfRhythms - rhythmsSoFar;
    for (int i = 0; i < numberOfRandomRhythms; i++) {
      int numberOfCategories = 2 + (int)((double)numberOfQuarters * (double)i / (double)numberOfRandomRhythms);
      Collection<QuarterCategory> categories = chooseCategories(numberOfCategories, categoryIterator);
      List<Quarter> quarters = chooseQuarters(categories);
      Optional<RandomRhythm> optionalRhythm = randomTies.apply(quarters);
      if (optionalRhythm.isPresent()) {
        result.add(optionalRhythm.get());
      }
    }
    return result;
  }

  private Collection<? extends AbstractRhythm> standardRhythms() {
    List<AbstractRhythm> result = new ArrayList<>();
    result.addAll(claves());
    result.add(charleston());
    result.addAll(bossas());
    result.add(choro());
    result.add(baiao());
    result.addAll(montunos());
    result.addAll(cascara());
    return result;
  }
  
  private Collection<? extends AbstractRhythm> cascara() {
    Quarter q1 = q(b2, b1, b1);
    Quarter q2 = q(r1, b2, b1);
    Quarter q3 = q(b2, b2);
    Quarter q4 = q(b1, b3);
    return List.of(
        new StandardRhythm("3/2 Cascara", repeat(4, q1, q2, q3, q4)),
        new StandardRhythm("2/3 Cascara", repeat(4, q3, q4, q1, q2))
        );
  }

  private Collection<? extends AbstractRhythm> montunos() {
    Quarter q1 = q(b2, b1, b1);
    Quarter q2 = q(r1, b2, b1);
    return List.of(
        new StandardRhythm("2/3 Montuno", repeat(4, q1, q2, q2, q2)),
        new StandardRhythm("3/2 Montuno", repeat(4, q2, q2, q1, q2))
      );
  }

  private StandardRhythm baiao() {
    Quarter q1 = q(b3, b1).tie();
    Quarter q2 = q(b2, b2);
    return new StandardRhythm("Baiao", repeat(8, q1, q2));
  }

  private StandardRhythm choro() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b2, b1, r1);
    return new StandardRhythm("Choro Variation", repeat(8, q1, q2));
  }

  private Collection<? extends AbstractRhythm> bossas() {
    Quarter q1 = q(b2, b2);
    Quarter q2 = q(r1, b1, r1, b1);
    return List.of(
          new StandardRhythm("Bossa Nova", repeat(8, q1, q2)),
          new StandardRhythm("Reverse Bossa Nova", repeat(8, q2, q1)),
          new StandardRhythm("Samba", repeat(4, q2, q1, q1, q2)),
          new StandardRhythm("Reverse Samba", repeat(4, q1, q2, q2, q1))
        );
  }

  private StandardRhythm charleston() {
    return new StandardRhythm("Carleston", repeat(8, q(b3, b1), q(r4)));
  }

  private Collection<? extends AbstractRhythm> claves() {
    Collection<AbstractRhythm> result = new ArrayList<>();
    Quarter q1 = q(r2, b2);
    Quarter q2 = q(b2, r2);
    Quarter q3 = q(b3, b1).tie();
    Quarter q4 = q(b2, b2);
    Quarter q2bossa = q(r1, b2, r1);
    Quarter q4rhumba = q(b3, b1);
    result.add(new StandardRhythm("2-3 Son Clave", repeat(4, q1, q2, q3, q4)));
    result.add(new StandardRhythm("3-2 Son Clave", repeat(4, q3, q4, q1, q2)));
    result.add(new StandardRhythm("2-3 Rhumba Clave", repeat(4, q1, q2, q3, q4rhumba)));
    result.add(new StandardRhythm("3-2 Rhumba Clave", repeat(4, q3, q4rhumba, q1, q2)));
    result.add(new StandardRhythm("2-3 Bossa Clave", repeat(4, q1, q2bossa, q3, q4)));
    result.add(new StandardRhythm("3-2 Bossa Clave", repeat(4, q3, q4, q1, q2bossa)));
    return result;
  }

  private Collection<? extends AbstractRhythm> basicRhythms(Function<List<Quarter>, Optional<RandomRhythm>> factory) {
    return quarterMap.keySet().stream()
      .map(Collections::singleton)
      .map(this::chooseQuarters)
      .map(factory)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  private List<Quarter> chooseQuarters(Collection<QuarterCategory> categories) {
    List<Quarter> validQuarters = categories.stream().flatMap(category -> quarterMap.get(category).stream()).collect(toList());
    Iterator<Quarter> iterator = Utils.randomLoopIterator(validQuarters);
    List<Quarter> result = new ArrayList<>();
    for (int i = 0; i < numberOfQuarters; i++) {
      result.add(iterator.next());
    }
    return result;
  }

  private Collection<QuarterCategory> chooseCategories(int numberOfCategories, Iterator<QuarterCategory> categoryIterator) {
    List<QuarterCategory> result = new ArrayList<>();
    for (int i = 0; i < numberOfCategories; i++) {
      result.add(categoryIterator.next());
    }
    return result;
  }

}
