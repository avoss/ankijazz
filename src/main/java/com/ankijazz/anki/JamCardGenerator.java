package com.ankijazz.anki;

import static com.ankijazz.midi.song.Ensembles.funk;
import static com.ankijazz.midi.song.SongFactory.Feature.AllKeys;
import static com.ankijazz.midi.song.SongFactory.Feature.EachKey;
import static com.ankijazz.midi.song.SongFactory.Feature.ExtTwoFiveOnes;
import static com.ankijazz.midi.song.SongFactory.Feature.JazzBlues;
import static com.ankijazz.midi.song.SongFactory.Feature.SomeKeys;
import static com.ankijazz.midi.song.SongFactory.Feature.Triads;
import static com.ankijazz.midi.song.SongFactory.Feature.TwoFiveOnes;
import static com.ankijazz.midi.song.SongFactory.Feature.Workouts;

import java.util.Set;
import java.util.function.Supplier;

import com.ankijazz.midi.song.Ensemble;
import com.ankijazz.theory.Note;

public class JamCardGenerator extends AbstractJamCardGenerator {

  
  public JamCardGenerator(String title, Note instrument, boolean withGuitar) {
    super(title, instrument, withGuitar);
  }

  @Override
  protected void addCards() {
    Set<Supplier<Ensemble>> ensembles =  Set.of(() -> funk(90)/*, () -> latin(125)*/);
    addCards(Set.of(Triads, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(Workouts, SomeKeys, AllKeys), ensembles);
    addCards(Set.of(TwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(ExtTwoFiveOnes, EachKey, AllKeys), ensembles);
    addCards(Set.of(JazzBlues, EachKey), ensembles);
  }


}
