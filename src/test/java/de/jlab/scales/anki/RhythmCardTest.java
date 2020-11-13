package de.jlab.scales.anki;

import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.rhythm.EventSequence;
import de.jlab.scales.rhythm.Rhythm;

public class RhythmCardTest {

  @Test
  public void testRhythmCard() {
    Card card = new RhythmCard(new RhythmModel(rhythm()));
    TestUtils.assertFileContentMatches(singletonList(card.getCsv()), getClass(), "RhythmCardTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(card.getHtml()), getClass(), "RhythmCardTest.html.txt");
  }

  private Rhythm rhythm() {
    EventSequence s1 = EventSequence.of(r2, b2);
    EventSequence s2 = EventSequence.of(b1, b1, r2);
    return Rhythm.of(asList(s1, s2), singleton(s1));
  }

}
