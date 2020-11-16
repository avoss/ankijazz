package de.jlab.scales.anki;

import static de.jlab.scales.lily.Direction.ASCENDING;
import static de.jlab.scales.lily.Direction.DESCENDING;
import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static java.lang.String.format;

import java.util.List;

import de.jlab.scales.lily.Clef;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.Scales;

public class ModesPracticeDeck extends AbstractDeck {

  public ModesPracticeDeck(Note instrument) {
    super(format("ModesPractice%sDeck", instrument.getName(FLAT)), format("AnkiJazz - Practice Modes (%s-Instrument)", instrument.getName(FLAT)));
    initialize(Clef.TREBLE, instrument);
  }

  public ModesPracticeDeck(Clef clef) {
    super(format("ModesPractice%sDeck", clef.getLabel()), format("AnkiJazz - Practice Modes (%s-Clef)", clef.getLabel()));
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
