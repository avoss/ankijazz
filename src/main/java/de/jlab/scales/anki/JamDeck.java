package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.funk;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.SongFactory.Feature.*;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;

import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.ProgressionFactory;
import de.jlab.scales.midi.song.SongFactory;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.midi.song.SongWrapper;
import de.jlab.scales.theory.Note;

public class JamDeck extends AbstractDeck<JamCard> {

  private final RenderContext context = RenderContext.ANKI;
  private final Note instrument;
  private boolean withGuitar;
  
  public JamDeck(String title, Note instrument, boolean withGuitar) {
    super(title, "Jam".concat(instrument.name()).concat(withGuitar ? "Guitar" : "").concat("Deck"));
    this.instrument = instrument;
    this.withGuitar = withGuitar;
    // addCards(Set.of(Test, AllKeys), Set.of(latin(125)));
    
    Set<Ensemble> ensembles = Set.of(funk(80), latin(125));
    addCards(Set.of(Triads, EachKey, AllKeys), ensembles);
    addCards(Set.of(Workouts, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(TwoFiveOnes, EachKey), ensembles);
    addCards(Set.of(ExtTwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(JazzBlues, EachKey), ensembles);
    System.out.println(String.format("Total number of cards: %d", getCards().size()));
  }

  private void addCards(Set<Feature> features, Set<Ensemble> ensembles) {
    int numberOfCards = 0;
    for (Ensemble ensemble: ensembles) {
      numberOfCards += addCards(ensemble, features);
    }
    System.out.println(String.format("%5d cards added for %s and %s", numberOfCards, features, ensembles));
  }

  private int addCards(Ensemble ensemble, Set<Feature> features) {
    int numberOfCards = 0;
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
      if (withGuitar) {
        for (FretboardPosition position : FretboardPosition.values()) {
          add(new JamCard(instrument, context, wrapper, ensemble, position));
          numberOfCards ++;
        }
      } else {
        add(new JamCard(instrument, context, wrapper, ensemble));
        numberOfCards ++;
      }
    }
    return numberOfCards;
  }

 
}
