package com.ankijazz.anki;

import static com.ankijazz.lily.Direction.ASCENDING;
import static com.ankijazz.lily.Direction.DESCENDING;
import static com.ankijazz.theory.ScaleUniverse.MODES;
import static com.ankijazz.theory.Scales.allKeys;
import static com.ankijazz.theory.Scales.commonModes;

import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

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
