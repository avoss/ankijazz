package de.jlab.scales.anki;

import static java.util.Collections.singletonList;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class PlayModesCardTest {

  @Test
  public void test() {
    Scale mode = Scales.CMajor.superimpose(Note.D);
    ScaleInfo info = ScaleUniverse.MODES.info(mode);
    PlayModesCard card = new PlayModesCard(new ScaleModel(info, false));
    TestUtils.assertFileContentMatches(singletonList(card.getCsv()), getClass(), "PlayModesCardTest.csv.txt");
    TestUtils.assertFileContentMatches(singletonList(card.getHtml()), getClass(), "PlayModesCardTest.html.txt");
  }

}
