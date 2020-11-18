package de.jlab.scales.rhythm;

import static de.jlab.scales.Utils.repeat;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.b3;
import static de.jlab.scales.rhythm.Event.r1;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Event.r4;
import static de.jlab.scales.rhythm.EventSequence.q;
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

  private final Map<EventSequenceCategory, Collection<EventSequence>> eventSequenceMap;
  private final int numberOfRhythms = 300;
  private final int numberOfSequences = 16;

  public RhythmGenerator() {
    this(new EventSequenceGenerator().getEventSequenceMap());
  }

  public RhythmGenerator(Map<EventSequenceCategory, Collection<EventSequence>> eventSequenceMap) {
    this.eventSequenceMap = eventSequenceMap;
  }
  
  static abstract class AbstractTies implements Function<List<EventSequence>, Optional<RandomRhythm>> {

    @Override
    public Optional<RandomRhythm> apply(List<EventSequence> sequences) {
      List<EventSequence> copy = new ArrayList<>();
      for (int i = 1; i < sequences.size(); i++) {
        EventSequence prev = sequences.get(i-1);
        EventSequence next = sequences.get(i);
        copy.add(canCreateTie(prev, next) && shouldCreateTie(prev, next) ? prev.tie() : prev);
      }
      return result(sequences);
    }
    
    boolean canCreateTie(EventSequence prev, EventSequence next) {
      if (prev.getNumberOfEvents() + next.getNumberOfEvents() < 3) {
        return false;
      }
      return prev.endsWithBeat() && next.startsWithBeat();
    }
    
    protected abstract boolean shouldCreateTie(EventSequence prev, EventSequence next);
    protected abstract Optional<RandomRhythm> result(List<EventSequence> sequences);
  }
  
  static class AllTies extends AbstractTies {

    @Override
    protected boolean shouldCreateTie(EventSequence prev, EventSequence next) {
      return true;
    }

    @Override
    protected Optional<RandomRhythm> result(List<EventSequence> sequences) {
      if (EventSequence.hasTies(sequences)) {
        return Optional.of(new RandomRhythm(sequences));
      }
      return Optional.absent();
    }
    
  }

  static class RandomTies extends AbstractTies {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    protected boolean shouldCreateTie(EventSequence prev, EventSequence next) {
      return random.nextBoolean(); 
    }

    @Override
    protected Optional<RandomRhythm> result(List<EventSequence> sequences) {
      return Optional.of(new RandomRhythm(sequences));
    }
    
  }

  static class NoTies extends AbstractTies {

    @Override
    protected boolean shouldCreateTie(EventSequence prev, EventSequence next) {
      return false;
    }

    @Override
    protected Optional<RandomRhythm> result(List<EventSequence> sequences) {
      return Optional.of(new RandomRhythm(sequences));
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
    EventSequence q1 = q(b3, b1).tie();
    EventSequence q2 = q(b2, b2).tie();
    EventSequence q3 = q(b1, b3).tie();
    EventSequence q4 = q(b3, b1);
    
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
    Iterator<EventSequenceCategory> categoryIterator = Utils.randomLoopIterator(eventSequenceMap.keySet());
    int numberOfRandomRhythms = numberOfRhythms - rhythmsSoFar;
    for (int i = 0; i < numberOfRandomRhythms; i++) {
      int numberOfCategories = 2 + (int)((double)numberOfSequences * (double)i / (double)numberOfRandomRhythms);
      Collection<EventSequenceCategory> categories = chooseCategories(numberOfCategories, categoryIterator);
      List<EventSequence> sequences = chooseSequences(categories);
      Optional<RandomRhythm> optionalRhythm = randomTies.apply(sequences);
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
    EventSequence q1 = q(b2, b1, b1);
    EventSequence q2 = q(r1, b2, b1);
    EventSequence q3 = q(b2, b2);
    EventSequence q4 = q(b1, b3);
    return List.of(
        new StandardRhythm("3/2 Cascara", repeat(4, q1, q2, q3, q4)),
        new StandardRhythm("2/3 Cascara", repeat(4, q3, q4, q1, q2))
        );
  }

  private Collection<? extends AbstractRhythm> montunos() {
    EventSequence q1 = q(b2, b1, b1);
    EventSequence q2 = q(r1, b2, b1);
    return List.of(
        new StandardRhythm("2/3 Montuno", repeat(4, q1, q2, q2, q2)),
        new StandardRhythm("3/2 Montuno", repeat(4, q2, q2, q1, q2))
      );
  }

  private StandardRhythm baiao() {
    EventSequence q1 = q(b3, b1).tie();
    EventSequence q2 = q(b2, b2);
    return new StandardRhythm("Baiao", repeat(8, q1, q2));
  }

  private StandardRhythm choro() {
    EventSequence q1 = q(r2, b2).tie();
    EventSequence q2 = q(b2, b1, r1);
    return new StandardRhythm("Choro Variation", repeat(8, q1, q2));
  }

  private Collection<? extends AbstractRhythm> bossas() {
    EventSequence q1 = q(b2, b2);
    EventSequence q2 = q(r1, b1, r1, b1);
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
    EventSequence q1 = q(r2, b2);
    EventSequence q2 = q(b2, r2);
    EventSequence q3 = q(b3, b1).tie();
    EventSequence q4 = q(b2, b2);
    EventSequence q2bossa = q(r1, b2, r1);
    EventSequence q4rhumba = q(b3, b1);
    result.add(new StandardRhythm("2-3 Son Clave", repeat(4, q1, q2, q3, q4)));
    result.add(new StandardRhythm("3-2 Son Clave", repeat(4, q3, q4, q1, q2)));
    result.add(new StandardRhythm("2-3 Rhumba Clave", repeat(4, q1, q2, q3, q4rhumba)));
    result.add(new StandardRhythm("3-2 Rhumba Clave", repeat(4, q3, q4rhumba, q1, q2)));
    result.add(new StandardRhythm("2-3 Bossa Clave", repeat(4, q1, q2bossa, q3, q4)));
    result.add(new StandardRhythm("3-2 Bossa Clave", repeat(4, q3, q4, q1, q2bossa)));
    return result;
  }

  private Collection<? extends AbstractRhythm> basicRhythms(Function<List<EventSequence>, Optional<RandomRhythm>> factory) {
    return eventSequenceMap.keySet().stream()
      .map(Collections::singleton)
      .map(this::chooseSequences)
      .map(factory)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  private List<EventSequence> chooseSequences(Collection<EventSequenceCategory> categories) {
    List<EventSequence> validSequences = categories.stream().flatMap(category -> eventSequenceMap.get(category).stream()).collect(toList());
    Iterator<EventSequence> iterator = Utils.randomLoopIterator(validSequences);
    List<EventSequence> result = new ArrayList<>();
    for (int i = 0; i < numberOfSequences; i++) {
      result.add(iterator.next());
    }
    return result;
  }

  private Collection<EventSequenceCategory> chooseCategories(int numberOfCategories, Iterator<EventSequenceCategory> categoryIterator) {
    List<EventSequenceCategory> result = new ArrayList<>();
    for (int i = 0; i < numberOfCategories; i++) {
      result.add(categoryIterator.next());
    }
    return result;
  }


}
