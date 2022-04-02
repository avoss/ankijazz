package de.jlab.scales.anki;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Quarter.q;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.lily.LilyMetronome;
import de.jlab.scales.lily.LilyMetronome.Tempo;
import de.jlab.scales.lily.LilyRhythm;
import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.Quarter;
import de.jlab.scales.rhythm.StandardRhythm;

public class RhythmCardTest {

  @Test
  public void testRhythmCard() {
    Tempo tempo = new LilyMetronome(5, 50, 120).tempo(83);
    Card card = new RhythmCard(rhythm(), tempo, LilyRhythm.Type.PIANO);
    assertFileContentMatches(card.getCsv(), getClass(), "RhythmCardTest.txt");
  }

  private AbstractRhythm rhythm() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b1, b1, r2);
    return new StandardRhythm("Test", List.of(q1, q2));
  }

}