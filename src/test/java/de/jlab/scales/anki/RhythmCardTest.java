package de.jlab.scales.anki;

import static de.jlab.scales.TestUtils.*;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.EventSequence;
import de.jlab.scales.rhythm.Rhythm;

public class RhythmCardTest {

  @Test
  public void testRhythmCard() {
    Card card = new RhythmCard(new RhythmModel(rhythm()));
    assertFileContentMatches(card.getCsv(), getClass(), "RhythmCardTest.csv.txt");
    assertFileContentMatches(card.getHtml(), getClass(), "RhythmCardTest.html.txt");
  }

  private Rhythm rhythm() {
    EventSequence s1 = EventSequence.of(r2, b2);
    EventSequence s2 = EventSequence.of(b1, b1, r2);
    return new Rhythm("Test", List.of(s1, s2), singleton(s1));
  }

}
