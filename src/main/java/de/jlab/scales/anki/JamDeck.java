package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.SongFactory.Feature.SomeKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;

import java.util.EnumSet;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.ProgressionFactory;
import de.jlab.scales.midi.song.SongFactory;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.midi.song.SongWrapper;
import de.jlab.scales.theory.Note;

public class JamDeck extends AbstractDeck<JamCard> {

  private final RenderContext context = RenderContext.ANKI;
  private final Note instrument;
  private boolean withGuitar;
  
  public JamDeck(String title, Set<Feature> features) {
    super(title);
    instrument = Note.C;
    addCards(features);
  }

  public JamDeck(String title, Note instrument, boolean withGuitar) {
    super(title, "JamDeck".concat(instrument.name()).concat(withGuitar ? "Guitar" : ""));
    this.instrument = instrument;
    this.withGuitar = withGuitar;
    //addCards(EnumSet.of(Test, AllKeys));
    addCards(EnumSet.of(Workouts, SomeKeys));
  }

  // TODO: someKeys instead of eachKey, increase tempo slowly
  // different random chords per fretboard position -> uuid good enough for assedId
  private void addCards(Set<Feature> features) {
    addCards(Ensembles.funk(70), features);
    addCards(Ensembles.latin(120), features);
  }

  private void addCards(Ensemble ensemble, Set<Feature> features) {
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
      if (withGuitar) {
        for (FretboardPosition position : FretboardPosition.values()) {
          add(new JamCard(instrument, context, wrapper, ensemble, position));
        }
      } else {
        add(new JamCard(instrument, context, wrapper, ensemble));
      }
    }
  }

 
}
