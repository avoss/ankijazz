package de.jlab.scales.rhythm;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

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
  
  public List<Rhythm> generate() {
    List<Rhythm> result = new ArrayList<>();
    Iterator<EventSequenceCategory> categoryIterator = Utils.randomLoopIterator(eventSequenceMap.keySet());
    for (int i = 0; i < numberOfRhythms; i++) {
      Collection<EventSequenceCategory> categories = chooseCategories(i, categoryIterator);
      List<EventSequence> events = chooseSequences(categories);
      result.add(new Rhythm(events, emptySet()));
    }
    return result;
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
