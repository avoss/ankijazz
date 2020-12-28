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

import de.jlab.scales.midi.song.BarFactory.SeventhBuilder;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class SongFactory {

  private Set<Feature> features;
  private Map<Feature, ProgressionFactory> progressionFactories;
  private Map<Feature, List<KeyFactory>> keyFactories;

  public enum Feature { Triads, SeventhChords, WithSubs, WithTwoFiveSubs, Major6251, Minor6251, Folk, SimpleBlues, JazzBlues, EachKey, AllKeys }
  

  @Getter
  static abstract class ProgressionFactory {
    private int numberOfBars;
    private Set<Feature> features;
    
    protected ProgressionFactory(int numberOfBars, Set<Feature> features) {
      this.numberOfBars = numberOfBars;
      this.features = features;
    }
    
    protected abstract List<Bar> create(KeySignature keySignature);
    
    protected BarFactory.Builder builder() {
      if (features.contains(Feature.SeventhChords) && features.contains(Feature.Triads)) {
        throw new IllegalStateException("cannot produce chord progressions with triads and seventh chords together, sorry");
      }
      if (features.contains(Feature.SeventhChords)) {
        return BarFactory.seventhChordBuilder();
      }
      if (features.contains(Feature.Triads)) {
        return BarFactory.triadsBuilder();
      }
      throw new IllegalStateException("must have either triads or seventh chords in features");
    }
    
    public int getNumberOfBars() {
      return numberOfBars;
    }
    
    protected boolean withSubs() {
      return features.contains(Feature.WithSubs);
    }
    
    protected boolean withTwoFiveSubs() {
      return features.contains(Feature.WithTwoFiveSubs);
    }

    BarFactory dominant7(Note root) {
      BarFactory.Builder builder = builder().withRoot(root).withDominant7();
      if (withSubs()) {
        builder = builder
            .withDominant7Subs()
            .withAltered()
            .withAlteredSubs();
      }
      if (withTwoFiveSubs()) {
        builder = builder
            .withTwoFiveSubs()
            .withTwoFiveAlteredSubs();
      }
      return builder.build();
    }

    BarFactory major7(Note root) {
      BarFactory.Builder builder = builder().withRoot(Note.C).withMajor7();
      if (withSubs()) {
        builder = builder.withMajor7Subs();
      }
      return builder.build();
    }


    BarFactory minor7(Note root) {
      BarFactory.Builder builder = builder().withRoot(root).withMinor7();
      if (withSubs()) {
        builder = builder
            .withMinor7Subs();
      }
      if (withTwoFiveSubs()) {
        builder = builder
            .withDominant7Subs()
            .withTwoFiveSubs();
      }
      return builder.build();
    }
    
  }
  
  // TODO getTitle() contains keysignature.notate(root)
  static class Major6251 extends ProgressionFactory {
    private BarFactory VI;
    private BarFactory II;
    private BarFactory V;
    private BarFactory I;
    
    protected Major6251(Set<Feature> features) {
      super(4, features);
      VI = minor7(Note.A);
      II = minor7(Note.D);
      V = dominant7(Note.G);
      I = major7(Note.C);
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
      super(4,features);
      VI = builder().withRoot(Note.F).withMajor7().build();
      II = builder().withRoot(Note.B).withMinor7b5().build();
      V = builder().withRoot(Note.E).withAltered().build();
      I = builder().withRoot(Note.A).withMinor7().build();
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
