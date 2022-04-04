package com.ankijazz.anki;

import static com.ankijazz.lily.Direction.ASCENDING;
import static com.ankijazz.lily.Direction.DESCENDING;
import static com.ankijazz.theory.Accidental.FLAT;
import static com.ankijazz.theory.ScaleUniverse.MODES;
import static com.ankijazz.theory.Scales.allKeys;
import static com.ankijazz.theory.Scales.commonModes;
import static java.lang.String.format;

import com.ankijazz.lily.Clef;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.ScaleInfo;

public class ModesPracticeDeck extends AbstractDeck<ModesPracticeCard> {

  public ModesPracticeDeck(Note instrument) {
    // TODO static factory method ...
    super(format("AnkiJazz - Practice Modes (%s-Instrument)", instrument.getName(FLAT)), format("ModesPractice%sDeck", instrument.getName(FLAT)));
    initialize(Clef.TREBLE, instrument);
  }

  public ModesPracticeDeck(Clef clef) {
    super(format("AnkiJazz - Practice Modes (%s-Clef)", clef.getLabel()), format("ModesPractice%sDeck", clef.getLabel()));
    initialize(clef, Note.C);
  }

  private void initialize(Clef clef, Note instrument) {
    for (Scale scale : allKeys(commonModes())) {
      for (ScaleInfo info : MODES.infos(scale)) {
        add(new ModesPracticeCard(new ScaleModel(info, DESCENDING, clef, instrument)));
        add(new ModesPracticeCard(new ScaleModel(info, ASCENDING, clef, instrument)));
      }
    }
  }
}
