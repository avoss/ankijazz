package de.jlab.scales.anki;

import java.util.EnumSet;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.ProgressionFactory;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongFactory;
import de.jlab.scales.midi.song.SongFactory.Feature;
import static de.jlab.scales.midi.song.SongFactory.Feature.*;

public class JamDeck extends AbstractDeck<JamCard> {

  private RenderContext context = RenderContext.ANKI;
  
  protected JamDeck(String title) {
    super(title);
    addCards(EnumSet.of(Workouts, AllKeys));
  }

  private void addCards(Set<Feature> features) {
    LoopIteratorFactory iteratorFactory = Utils.randomLoopIteratorFactory();
    SongFactory factory = new SongFactory(iteratorFactory, new ProgressionFactory(iteratorFactory), features);
    for (Song song: factory.generate(context.getNumberOfBars())) {
      add(new JamCard(context, song, Ensembles.funk(80)));
      add(new JamCard(context, song, Ensembles.latin(120)));
    }
  }

 
}
