package de.jlab.scales.anki;

import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class PlayModesGuitarDeck extends AbstractDeck {

  public PlayModesGuitarDeck() {
    super("AnkiJazz - Modes Practice (Guitar)");

    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        for (FretboardPosition position : FretboardPosition.values()) {
          add(new PlayModesGuitarCard(new ScaleModel(info, true), position));
          add(new PlayModesGuitarCard(new ScaleModel(info, false), position));
        }
      }
    }
  }
  
  /**
   * skip cards, otherwise it is too many / html too big
   */
  public List<Card> getHtmlCards() {
    Predicate<Card> filter = new Predicate<Card>() {
        int counter = 0;

        @Override
        public boolean test(Card t) {
          return counter++ % 13 == 0;
        }
    };
    
    return getCards().stream().filter(filter).collect(Collectors.toList());
  }


}
