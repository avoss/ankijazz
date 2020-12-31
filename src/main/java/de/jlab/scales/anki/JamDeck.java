package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;

import java.util.EnumSet;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.ProgressionFactory;
import de.jlab.scales.midi.song.SongFactory;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.midi.song.SongWrapper;

public class JamDeck extends AbstractDeck<JamCard> {

  private RenderContext context = RenderContext.ANKI;
  
  protected JamDeck(String title, Set<Feature> features) {
    super(title);
    addCards(features);
  }

  private void addCards(Set<Feature> features) {
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (SongWrapper wrapper: factory.generate(context.getNumberOfBars())) {
      add(new JamCard(context, wrapper, Ensembles.funk(80)));
      add(new JamCard(context, wrapper, Ensembles.latin(120)));
    }
  }

 
}
