package de.jlab.scales.rhythm;

import static de.jlab.scales.Utils.repeat;
import static de.jlab.scales.rhythm.Event.*;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.jlab.scales.Utils;

public class RhythmGenerator {

  private final Map<EventSequenceCategory, Collection<EventSequence>> eventSequenceMap;
  private final int numberOfRhythms = 100;
  private final int numberOfSequences = 16;
  private final double tieProbability = 0.5;

  public RhythmGenerator() {
    this(new EventSequences().getEventSequenceMap());
  }

  public RhythmGenerator(Map<EventSequenceCategory, Collection<EventSequence>> eventSequenceMap) {
    this.eventSequenceMap = eventSequenceMap;
  }
  
  
  Function<List<EventSequence>, Rhythm> noTies = s -> new Rhythm("Basic", s, emptySet());
  
  Function<List<EventSequence>, Rhythm> withTies = sequences -> {
    Set<EventSequence> ties = new LinkedHashSet<>();
    for (int i = 1; i < sequences.size(); i++) {
      EventSequence prev = sequences.get(i-1);
      EventSequence next = sequences.get(i);
      if (prev.getNumberOfEvents() + next.getNumberOfEvents() < 3) {
        continue;
      }
      if (prev.endsWithBeat() && next.startsWithBeat()) {
        ties.add(prev);
      }
    }
    if (ties.isEmpty()) {
      return null;
    }
    return new Rhythm("Basic", sequences, ties);
  };
  
  public List<Rhythm> generate() {
    List<Rhythm> result = new ArrayList<>();
//    result.addAll(basicRhythms(noTies));
//    result.addAll(basicRhythms(withTies));
    result.addAll(standardRhythms());
//    result.addAll(randomRhythms(result.size()));
    return result;
  }

  private Collection<? extends Rhythm> randomRhythms(int rhythmsSoFar) {
    List<Rhythm> result = new ArrayList<>();
    Iterator<EventSequenceCategory> categoryIterator = Utils.randomLoopIterator(eventSequenceMap.keySet());
    for (int i = rhythmsSoFar; i < numberOfRhythms; i++) {
      Collection<EventSequenceCategory> categories = chooseCategories(i, categoryIterator);
      List<EventSequence> events = chooseSequences(categories);
      result.add(new Rhythm("Random", events, emptySet()));
    }
    return result;
  }

  private List<Rhythm> standardRhythms() {
    List<Rhythm> result = new ArrayList<>();
    result.addAll(claves());
    return result;
  }
  
  private Collection<Rhythm> claves() {
    Collection<Rhythm> result = new ArrayList<>();
    EventSequence q1 = q(r2, b2);
    EventSequence q2 = q(b2, r2);
    EventSequence q3 = q(b3, b1);
    EventSequence q4 = q(b2, b2);
    EventSequence q2bossa = q(r1, b2, r1);
    EventSequence q4rhumba = q(b3, b1);
    result.add(new Rhythm("2-3 Son Clave", repeat(4, q1, q2, q3, q4), Set.of(q3)));
    result.add(new Rhythm("3-2 Son Clave", repeat(4, q3, q4, q1, q2), Set.of(q3)));
    result.add(new Rhythm("2-3 Rhumba Clave", repeat(4, q1, q2, q3, q4rhumba), Set.of(q3)));
    result.add(new Rhythm("3-2 Rhumba Clave", repeat(4, q3, q4rhumba, q1, q2), Set.of(q3)));
    result.add(new Rhythm("2-3 Bossa Clave", repeat(4, q1, q2bossa, q3, q4), Set.of(q3)));
    result.add(new Rhythm("3-2 Bossa Clave", repeat(4, q3, q4, q1, q2bossa), Set.of(q3)));
    return result;
  }

  private EventSequence q(Event ... events) {
    return new EventSequence(events);
  }
  
  private List<Rhythm> basicRhythms(Function<List<EventSequence>, Rhythm> factory) {
    return eventSequenceMap.keySet().stream()
      .map(Collections::singleton)
      .map(this::chooseSequences)
      .map(factory)
      .filter(r -> r != null)
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

  private Collection<EventSequenceCategory> chooseCategories(int rhythmIndex, Iterator<EventSequenceCategory> categoryIterator) {
    List<EventSequenceCategory> result = new ArrayList<>();
    int numberOfCategories = 1 + (int)((double)numberOfSequences * (double)rhythmIndex / (double)numberOfRhythms);
    for (int i = 0; i < numberOfCategories; i++) {
      result.add(categoryIterator.next());
    }
    return result;
  }


}
