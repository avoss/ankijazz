package com.ankijazz.anki;

import static com.ankijazz.TestUtils.assertFileContentMatches;
import static com.ankijazz.rhythm.Event.b1;
import static com.ankijazz.rhythm.Event.b2;
import static com.ankijazz.rhythm.Event.r2;
import static com.ankijazz.rhythm.Quarter.q;

import java.util.List;

import org.junit.Test;

import com.ankijazz.anki.Card;
import com.ankijazz.anki.RhythmCard;
import com.ankijazz.lily.LilyMetronome;
import com.ankijazz.lily.LilyRhythm;
import com.ankijazz.lily.LilyMetronome.Tempo;
import com.ankijazz.rhythm.AbstractRhythm;
import com.ankijazz.rhythm.Quarter;
import com.ankijazz.rhythm.StandardRhythm;

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
