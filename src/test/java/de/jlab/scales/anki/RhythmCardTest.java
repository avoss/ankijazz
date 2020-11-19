package de.jlab.scales.anki;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Quarter.q;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.Quarter;
import de.jlab.scales.rhythm.StandardRhythm;

public class RhythmCardTest {

  @Test
  public void testRhythmCard() {
    Card card = new RhythmCard(rhythm());
    assertFileContentMatches(card.getCsv(), getClass(), "RhythmCardTest.csv.txt");
    assertFileContentMatches(card.getHtml(), getClass(), "RhythmCardTest.html.txt");
  }

  private AbstractRhythm rhythm() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b1, b1, r2);
    return new StandardRhythm("Test", List.of(q1, q2));
  }

}
