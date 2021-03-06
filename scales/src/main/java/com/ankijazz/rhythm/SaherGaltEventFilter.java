package com.ankijazz.rhythm;

import java.util.function.Predicate;


// https://www.youtube.com/watch?reload=9&v=Ri0w4gLeeZE
// https://www.youtube.com/watch?v=RuvA4b_2pk0&feature=youtu.be
// TODO: why only first note replaced with rest? why not any note?
public class SaherGaltEventFilter implements Predicate<Quarter> {

  @Override
  public boolean test(Quarter quarter) {
    // either no rests, or first event is a rest
    int numberOfRests = (int) quarter.getEvents().stream().filter(e -> !e.isBeat()).count();
    return numberOfRests == 0 || (quarter.getNumberOfEvents() > 1 && numberOfRests == 1 && !quarter.startsWithBeat());
  }

}
