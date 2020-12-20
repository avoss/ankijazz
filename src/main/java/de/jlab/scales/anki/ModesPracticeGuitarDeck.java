package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static de.jlab.scales.lily.Direction.DESCENDING;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;

import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class ModesPracticeGuitarDeck extends AbstractDeck<ModesPracticeGuitarCard> {

  public ModesPracticeGuitarDeck() {
    super("AnkiJazz - Modes Practice (Guitar)");

    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        for (FretboardPosition position : FretboardPosition.values()) {
          add(new ModesPracticeGuitarCard(new ScaleModel(info, DESCENDING), position));
          add(new ModesPracticeGuitarCard(new ScaleModel(info, ASCENDING), position));
        }
      }
    }
  }
  

}
