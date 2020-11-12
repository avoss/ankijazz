package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static de.jlab.scales.lily.Direction.DESCENDING;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;

import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class ModesPracticeDeck extends AbstractDeck {

  public ModesPracticeDeck(Clef clef) {
    super("PlayModesDeck" + clef.getLabel(), "AnkiJazz - Modes Practice");
    
    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        add(new ModesPracticeCard(new ScaleModel(info, DESCENDING, clef)));
        add(new ModesPracticeCard(new ScaleModel(info, ASCENDING, clef)));
      }
    }
  }

}
