package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.midi.song.ProgressionFactory.Progression;
import de.jlab.scales.midi.song.ProgressionFactory.ProgressionSet;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import lombok.Builder;
import lombok.Data;

public class SongFactory {
  private Set<Feature> features;
  private Map<Feature, List<KeyFactory>> keyFactories = new HashMap<>();
  private Map<Feature, ProgressionSet> progressionSets = new HashMap<>();
  private LoopIteratorFactory iteratorFactory;

  public enum Feature { Test, Triads, EachKey, AllKeys }

  @Data
  @Builder
  static class KeyFactory {
    private final Iterator<KeySignature> signatures;
    private final int numberOfKeys;
    
    static List<KeyFactory> eachKey(LoopIteratorFactory iteratorFactory) {
      List<KeyFactory> keys = new ArrayList<>();
      for (Note root : Note.values()) {
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          keys.add(new KeyFactory(iteratorFactory.iterator(singletonList(keySignature)), 1));
        }
      }
      return keys;
    }

    static List<KeyFactory> allKeys(LoopIteratorFactory iteratorFactory) {
      List<KeySignature> keys = new ArrayList<>();
      for (Note root : Note.values()) {
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          keys.add(keySignature);
        }
      }
      return singletonList(new KeyFactory(iteratorFactory.iterator(keys), keys.size()));
    }
  }
  
  public SongFactory(LoopIteratorFactory iteratorFactory, ProgressionFactory progressionFactory, Set<Feature> features) {
    this.iteratorFactory = iteratorFactory;
    this.features = features;
    
    keyFactories.put(AllKeys, KeyFactory.allKeys(iteratorFactory));
    keyFactories.put(EachKey, KeyFactory.eachKey(iteratorFactory));
    
    for (ProgressionSet set: progressionFactory.getProgressionSets()) {
      Feature feature = Feature.valueOf(set.getId());
      progressionSets.put(feature, set);
    }
    
  }
  
  
  public List<Song> generate(int numberOfBars) {
    List<Song> songs = new ArrayList<>();
    features.stream().map(keyFactories::get).filter(Objects::nonNull).flatMap(List::stream).forEach(key -> {
      features.stream().map(progressionSets::get).filter(Objects::nonNull).flatMap(set -> set.getProgressions().stream()).forEach(progression -> {
        songs.addAll(play(key, progression, numberOfBars));
        
      });
    });
    return songs;
  }

  private List<Song> play(KeyFactory key, Progression progression, int numberOfBarsInSong) {
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
