package de.jlab.scales.midi.song;

import static de.jlab.scales.Utils.loopIterator;
import static de.jlab.scales.Utils.randomLoopIterator;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Major6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.Minor6251;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import lombok.Builder;
import lombok.Data;

public class SongFactory {

  private Set<Feature> features;
  private Map<Feature, ProgressionFactory> progressionFactories;
  private Map<Feature, List<KeyFactory>> keyFactories;

  public enum Feature { Triads, SeventhChords, WithSubstitutions, Major6251, Minor6251, Folk, SimpleBlues, JazzBlues, EachKey, AllKeys }
  

  static abstract class ProgressionFactory {
    private int numberOfBars;
    protected ProgressionFactory(int numberOfBars) {
      this.numberOfBars = numberOfBars;
    }
    
    protected abstract List<Bar> create(KeySignature keySignature);
    
    public int getNumberOfBars() {
      return numberOfBars;
    }
  }
  
  // TODO getTitle() contains keysignature.notate(root)
  static class Major6251 extends ProgressionFactory {
    private BarFactory VI;
    private BarFactory II;
    private BarFactory V;
    private BarFactory I;
    
    protected Major6251(Set<Feature> features) {
      super(4);
      VI = BarFactory.seventhChordBuilder(Note.A).withMinor7().build();
      II = BarFactory.seventhChordBuilder(Note.D).withMinor7().build();
      V = BarFactory.seventhChordBuilder(Note.G)
          .withDominant7()
          .withTwoFiveAlteredSubs()
          .build();
      I = BarFactory.seventhChordBuilder(Note.C).withMajor7().build();
    }
    
    @Override
    protected List<Bar> create(KeySignature keySignature) {
      return List.of(
          VI.next(keySignature),
          II.next(keySignature),
          V.next(keySignature),
          I.next(keySignature));
    }
  }

  // TODO getTitle() contains keysignature.notate(root.transpose(-3)) // minor
  static class Minor6251 extends ProgressionFactory {
    private BarFactory VI;
    private BarFactory II;
    private BarFactory V;
    private BarFactory I;
    protected Minor6251(Set<Feature> features) {
      super(4);
      VI = BarFactory.seventhChordBuilder(Note.F).withMajor7().build();
      II = BarFactory.seventhChordBuilder(Note.B).withMinor7b5().build();
      V = BarFactory.seventhChordBuilder(Note.E).withAltered().build();
      I = BarFactory.seventhChordBuilder(Note.A).withMinor7().build();
    }
    @Override
    protected List<Bar> create(KeySignature keySignature) {
      return List.of(
          VI.next(keySignature),
          II.next(keySignature),
          V.next(keySignature),
          I.next(keySignature));
    }
  }
  
  @Data
  @Builder
  static class KeyFactory {
    private final Iterator<KeySignature> signatures;
    private final int numberOfKeys;

    static List<KeyFactory> eachKey() {
      List<KeyFactory> keys = new ArrayList<>();
      for (Note root : Note.values()) {
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          keys.add(new KeyFactory(loopIterator(singletonList(keySignature)), 1));
        }
      }
      return keys;
    }

    static List<KeyFactory> allKeys() {
      List<KeySignature> keys = new ArrayList<>();
      for (Note root : Note.values()) {
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          keys.add(keySignature);
        }
      }
      return singletonList(new KeyFactory(randomLoopIterator(keys), keys.size()));
    }
  }
  
  public SongFactory(Set<Feature> features) {
    this.features = features;
    
    progressionFactories = new HashMap<>();
    progressionFactories.put(Major6251, new Major6251(features));
    progressionFactories.put(Minor6251, new Minor6251(features));
    
    keyFactories = new HashMap<>();
    keyFactories.put(AllKeys, KeyFactory.allKeys());
    keyFactories.put(EachKey, KeyFactory.eachKey());
  }
  
  
  public List<Song> generate(int numberOfBars) {
    List<Song> songs = new ArrayList<>();
    features.stream().map(keyFactories::get).filter(Objects::nonNull).flatMap(List::stream).forEach(key -> { 
      features.stream().map(progressionFactories::get).filter(Objects::nonNull).forEach(progression -> {
        songs.addAll(play(key, progression, numberOfBars));
      });
    });
    return songs;
    
  }

  private Collection<? extends Song> play(KeyFactory key, ProgressionFactory progression, int numberOfBarsInSong) {
    int numberOfProgressions = numberOfBarsInSong / progression.getNumberOfBars();
    int numberOfSongs = key.numberOfKeys / numberOfProgressions;
    if ((key.numberOfKeys % numberOfProgressions) != 0) {
      numberOfSongs += 1;
    }
    List<Song> songs = new ArrayList<>();
    for (int i = 0; i < numberOfSongs; i++) {
      List<Bar> bars = new ArrayList<>();
      for (int j = 0; j < numberOfProgressions; j++) {
        bars.addAll(progression.create(key.getSignatures().next()));
      }
      songs.add(new Song(bars));
    }
    return songs;
  }

}
