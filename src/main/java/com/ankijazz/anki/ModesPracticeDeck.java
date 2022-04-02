package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static de.jlab.scales.lily.Direction.DESCENDING;
import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static java.lang.String.format;

import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

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
