package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.SongFactory.Feature.*;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Major6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.SeventhChords;

import java.util.EnumSet;
import java.util.Set;

import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongFactory;
import de.jlab.scales.midi.song.SongFactory.Feature;

public class JamDeck extends AbstractDeck<JamCard> {

  private RenderContext context = RenderContext.ANKI;
  
  protected JamDeck(String title) {
    super(title);
    addCards();
  }

  private void addCards() {
//    addCards(EnumSet.of(SeventhChords, WithSubstitutions, Major6251, Minor6251, EachKey, AllKeys));
    
//    addCards(EnumSet.of(ShellChords, Major6251, Minor6251, AllKeys, EachKey));
//    addCards(EnumSet.of(Triads, Major6251, Minor6251, AllKeys, EachKey));
    addCards(EnumSet.of(SeventhChords, WithSubs,  Major6251, AllKeys, EachKey));
  }

  private void addCards(Set<Feature> features) {
    SongFactory factory = new SongFactory(features);
    for (Song song: factory.generate(context.getNumberOfBars())) {
      add(new JamCard(context, song, Ensembles.funk(80)));
      //add(new JamCard(context, song, Ensembles.latin(120)));
    }
  }

 
}
