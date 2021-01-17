package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.funk;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.ExtTwoFiveOnes;
import static de.jlab.scales.midi.song.SongFactory.Feature.JazzBlues;
import static de.jlab.scales.midi.song.SongFactory.Feature.SomeKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Triads;
import static de.jlab.scales.midi.song.SongFactory.Feature.TwoFiveOnes;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;

import java.util.Set;
import java.util.function.Supplier;

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
    // addCards(Set.of(Test, AllKeys), () -> Set.of(latin(125)));
    
    Set<Supplier<Ensemble>> ensembles =  Set.of(() -> funk(80), () -> latin(125));
    addCards(Set.of(Triads, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(Workouts, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(TwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(ExtTwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(JazzBlues, EachKey), ensembles);
    System.out.println(String.format("Total number of cards: %d", getCards().size()));
  }

  private void addCards(Set<Feature> features, Set<Supplier<Ensemble>> ensembles) {
    int numberOfCards = 0;
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
      if (withGuitar) {
        for (FretboardPosition position : FretboardPosition.values()) {
          for (Supplier<Ensemble> ensemble : ensembles) {
            add(new JamCard(instrument, context, wrapper, ensemble, position));
            numberOfCards ++;
          }
        }
      } else {
        for (Supplier<Ensemble> ensemble : ensembles) {
          add(new JamCard(instrument, context, wrapper, ensemble));
          numberOfCards ++;
        }
      }
    }
    System.out.println(String.format("%5d cards added for %s", numberOfCards, features));
  }

 
}
