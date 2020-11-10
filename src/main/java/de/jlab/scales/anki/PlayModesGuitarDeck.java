package de.jlab.scales.anki;

import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.*;

import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class PlayModesGuitarDeck extends AbstractDeck {

  public PlayModesGuitarDeck() {
    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        add(new PlayModesCard(new ScaleModel(info, true)));
        add(new PlayModesCard(new ScaleModel(info, false)));
      }
    }
  }

}
