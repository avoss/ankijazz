package de.jlab.scales.gn;

import static de.jlab.scales.gn.Instrument.Guitar;
import static de.jlab.scales.gn.Type.Solo;
import static de.jlab.scales.gn.Type.Song;

import java.util.List;

import de.jlab.scales.anki.AbstractDeck;

public class GnDeck extends AbstractDeck {

  public GnDeck() {
    super("Groovin Nana Set List");
    for (GnSong song: List.of(GnSong.values())) {
      add(new GnCard(song, Song, Guitar));
      if (song.hasGuitarSolo()) {
        add(new GnCard(song, Solo, Guitar));
      }
    }
  }

}
