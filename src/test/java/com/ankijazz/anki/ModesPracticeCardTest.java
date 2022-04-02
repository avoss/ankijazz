package com.ankijazz.anki;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.anki.Card;
import com.ankijazz.anki.FretboardPosition;
import com.ankijazz.anki.ModesPracticeCard;
import com.ankijazz.anki.ModesPracticeGuitarCard;
import com.ankijazz.anki.ScaleModel;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;
import com.ankijazz.theory.ScaleUniverse;
import com.ankijazz.theory.Scales;

public class ModesPracticeCardTest {

  @Test
  public void testPlayModes() {
    Card card = new ModesPracticeCard(model());
    TestUtils.assertFileContentMatches(card.getCsv(), getClass(), "ModesPracticeCardTest.txt");
  }

  @Test
  public void testPlayModesGuitar() {
    Card card = new ModesPracticeGuitarCard(model(), FretboardPosition.HIGH);
    TestUtils.assertFileContentMatches(card.getCsv(), getClass(), "ModesPracticeGuitarCardTest.txt");
  }
  
  private ScaleModel model() {
    Scale mode = Scales.CMajor.superimpose(Note.D);
    ScaleInfo info = ScaleUniverse.MODES.findFirstOrElseThrow(mode);
    return new ScaleModel(info);
  }

}
