package com.ankijazz.anki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.ankijazz.Utils;
import com.ankijazz.Utils.LoopIteratorFactory;
import com.ankijazz.midi.song.Ensemble;
import com.ankijazz.midi.song.ProgressionFactory;
import com.ankijazz.midi.song.SongFactory;
import com.ankijazz.midi.song.SongWrapper;
import com.ankijazz.midi.song.SongFactory.Feature;
import com.ankijazz.sheet.RenderContext;
import com.ankijazz.theory.Note;

public abstract class AbstractJamCardGenerator implements CardGenerator<JamCard> {

  private final RenderContext context = RenderContext.ANKI;
  private final Note instrument;
  private final boolean withGuitar;
  private final String title;
  private final String fileName;
  private final List<JamCard> cards = new ArrayList<>();
  private final LoopIteratorFactory iteratorFactory;
  private final Iterator<String> stringSetIterator;
  
  protected AbstractJamCardGenerator(String title, Note instrument, boolean withGuitar) {
    this(title, instrument, withGuitar, Utils.randomLoopIteratorFactory());
  }
  
  protected AbstractJamCardGenerator(String title, Note instrument, boolean withGuitar, LoopIteratorFactory iteratorFactory) {
    this.title = title;
    this.instrument = instrument;
    this.withGuitar = withGuitar;
    this.iteratorFactory = iteratorFactory;
    this.fileName = "Jam".concat(instrument.name()).concat(withGuitar ? "Guitar" : "").concat("Deck");
    this.stringSetIterator = iteratorFactory.iterator(List.of("High String Set", "Low String Set"));

    addCards();
    
    System.out.println(String.format("Total number of cards: %d", cards.size()));
  }

  protected abstract void addCards();

  protected void addCards(Set<Feature> features, Set<Supplier<Ensemble>> ensembles) {
    int numberOfCards = 0;
    if (withGuitar) {
      for (FretboardPosition position : FretboardPosition.values()) {
        for (Supplier<Ensemble> ensemble : ensembles) {
          SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
          for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
            cards.add(JamCard.builder()
                .instrument(instrument)
                .context(context)
                .wrapper(wrapper)
                .ensembleSupplier(ensemble)
                .position(position)
                .stringSet(features.contains(Feature.Triads) ? stringSetIterator.next() : "")
                .build());
            numberOfCards ++;
          }
        }
      }
    } else {
      for (Supplier<Ensemble> ensemble : ensembles) {
        SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
        for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
          cards.add(JamCard.builder()
              .instrument(instrument)
              .context(context)
              .wrapper(wrapper)
              .ensembleSupplier(ensemble)
              .build());
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
