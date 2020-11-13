package de.jlab.scales.rhythm;

import java.util.function.Predicate;


// https://www.youtube.com/watch?reload=9&v=Ri0w4gLeeZE
// https://www.youtube.com/watch?v=RuvA4b_2pk0&feature=youtu.be

public class SaherGaltEventSequenceFilter implements Predicate<EventSequence> {

  @Override
  public boolean test(EventSequence sequence) {
    // either no rests, or first event is a rest
    int numberOfRests = (int) sequence.getEvents().stream().filter(e -> !e.isBeat()).count();
    return numberOfRests == 0 || (sequence.getNumberOfEvents() > 1 && numberOfRests == 1 && !sequence.startsWithBeat());
  }

}
