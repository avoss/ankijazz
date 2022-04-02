package de.jlab.scales.anki;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

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