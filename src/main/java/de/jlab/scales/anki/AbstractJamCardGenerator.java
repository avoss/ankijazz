package de.jlab.scales.anki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

public abstract class AbstractJamCardGenerator implements CardGenerator<JamCard> {

  private final RenderContext context = RenderContext.ANKI;
  private final Note instrument;
  private final boolean withGuitar;
  private final String title;
  private final String fileName;
  private final List<JamCard> cards = new ArrayList<>();
  
  protected AbstractJamCardGenerator(String title, Note instrument, boolean withGuitar) {
    this.title = title;
    this.instrument = instrument;
    this.withGuitar = withGuitar;
    this.fileName = "Jam".concat(instrument.name()).concat(withGuitar ? "Guitar" : "").concat("Deck");

    addCards();
    
    System.out.println(String.format("Total number of cards: %d", cards.size()));
  }

  protected abstract void addCards();

  protected void addCards(Set<Feature> features, Set<Supplier<Ensemble>> ensembles) {
    int numberOfCards = 0;
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
      if (withGuitar) {
        for (FretboardPosition position : FretboardPosition.values()) {
          for (Supplier<Ensemble> ensemble : ensembles) {
            cards.add(new JamCard(instrument, context, wrapper, ensemble, position));
            numberOfCards ++;
          }
        }
      } else {
        for (Supplier<Ensemble> ensemble : ensembles) {
          cards.add(new JamCard(instrument, context, wrapper, ensemble));
          numberOfCards ++;
        }
      }
    }
    System.out.println(String.format("%5d cards added for %s", numberOfCards, features));
  }
  
  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  @Override
  public Collection<? extends JamCard> generate() {
    return cards;
  }

}
