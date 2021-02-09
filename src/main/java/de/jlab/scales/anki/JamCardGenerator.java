package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.funk;
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

import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.theory.Note;

public class JamCardGenerator extends AbstractJamCardGenerator {

  
  public JamCardGenerator(String title, Note instrument, boolean withGuitar) {
    super(title, instrument, withGuitar);
  }

  @Override
  protected void addCards() {
    Set<Supplier<Ensemble>> ensembles =  Set.of(() -> funk(80) /*, () -> latin(125) */);
    addCards(Set.of(Triads, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(Workouts, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(TwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(ExtTwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(JazzBlues, EachKey), ensembles);
  }


}
