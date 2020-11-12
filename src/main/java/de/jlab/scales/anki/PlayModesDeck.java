package de.jlab.scales.anki;

import static de.jlab.scales.anki.Direction.ASCENDING;
import static de.jlab.scales.anki.Direction.DESCENDING;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;

import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class PlayModesDeck extends AbstractDeck {

  public PlayModesDeck() {
    super("AnkiJazz - Modes Practice");
    
    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        add(new PlayModesCard(new ScaleModel(info, DESCENDING)));
        add(new PlayModesCard(new ScaleModel(info, ASCENDING)));
      }
    }
  }

}
