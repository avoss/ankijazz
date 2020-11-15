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
  private final int numberOfRhythms = 250;
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
    result.addAll(basicRhythms(noTies));
    result.addAll(basicRhythms(withTies));
    result.addAll(standardRhythms());
    result.addAll(randomRhythms(result.size()));
    return result;
  }

  private Collection<? extends Rhythm> randomRhythms(int rhythmsSoFar) {
    List<Rhythm> result = new ArrayList<>();
    Iterator<EventSequenceCategory> categoryIterator = Utils.randomLoopIterator(eventSequenceMap.keySet());
    int numberOfRandomRhythms = numberOfRhythms - rhythmsSoFar;
    for (int i = 0; i < numberOfRandomRhythms; i++) {
      int numberOfCategories = 2 + (int)((double)numberOfSequences * (double)i / (double)numberOfRandomRhythms);
      Collection<EventSequenceCategory> categories = chooseCategories(numberOfCategories, categoryIterator);
      List<EventSequence> events = chooseSequences(categories);
      result.add(new Rhythm("Random", events, emptySet()));
    }
    return result;
  }

  private List<Rhythm> standardRhythms() {
    List<Rhythm> result = new ArrayList<>();
    result.addAll(claves());
    result.add(charleston());
    result.addAll(bossas());
    result.add(choro());
    result.add(baiao());
    result.addAll(montunos());
    result.addAll(cascara());
    return result;
  }
  
  private Collection<? extends Rhythm> cascara() {
    EventSequence q1 = q(b2, b1, b1);
    EventSequence q2 = q(r1, b2, b1);
    EventSequence q3 = q(b2, b2);
    EventSequence q4 = q(b1, b3);
    return List.of(
        new Rhythm("3/2 Cascara", repeat(4, q1, q2, q3, q4), emptySet()),
        new Rhythm("2/3 Cascara", repeat(4, q3, q4, q1, q2), emptySet())
        );
  }

  private Collection<? extends Rhythm> montunos() {
    EventSequence q1 = q(b2, b1, b1);
    EventSequence q2 = q(r1, b2, b1);
    return List.of(
        new Rhythm("2/3 Montuno", repeat(4, q1, q2, q2, q2), emptySet()),
        new Rhythm("3/2 Montuno", repeat(4, q2, q2, q1, q2), emptySet())
      );
  }

  private Rhythm baiao() {
    EventSequence q1 = q(b3, b1);
    EventSequence q2 = q(b2, b2);
    return new Rhythm("Baiao", repeat(8, q1, q2), Set.of(q1));
  }

  private Rhythm choro() {
    return new Rhythm("Choro Variation", repeat(8, q(r2, b2), q(r2, b1, r1)), emptySet());
  }

  private Collection<? extends Rhythm> bossas() {
    EventSequence q1 = q(b2, b2);
    EventSequence q2 = q(r1, b1, r1, b1);
    return List.of(
          new Rhythm("Bossa Nova", repeat(8, q1, q2), emptySet()),
          new Rhythm("Reverse Bossa Nova", repeat(8, q2, q1), emptySet()),
          new Rhythm("Samba", repeat(4, q2, q1, q1, q2), emptySet()),
          new Rhythm("Reverse Samba", repeat(4, q1, q2, q2, q1), emptySet())
        );
  }

  private Rhythm charleston() {
    return new Rhythm("Carleston", repeat(8, q(b3, b1), q(r4)), emptySet());
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

  private Collection<EventSequenceCategory> chooseCategories(int numberOfCategories, Iterator<EventSequenceCategory> categoryIterator) {
    List<EventSequenceCategory> result = new ArrayList<>();
    for (int i = 0; i < numberOfCategories; i++) {
      result.add(categoryIterator.next());
    }
    return result;
  }


}
