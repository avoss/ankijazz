package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.SomeKeys;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.midi.song.ProgressionFactory.Progression;
import de.jlab.scales.midi.song.ProgressionFactory.ProgressionSet;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import lombok.Data;

public class SongFactory {
  private Set<Feature> features;
  private Map<Feature, List<KeyFactory>> keyFactories = new HashMap<>();
  private Map<Feature, ProgressionSet> progressionSets = new HashMap<>();
  private Set<Song> songsGeneratedSoFar = new HashSet<>();

  public enum Feature { Test, Workouts, Triads, EachKey, SomeKeys, AllKeys }

  @Data
  static class KeyFactory {
    private final String title;
    private final Iterator<KeySignature> signatures;
    private final int numberOfKeys;
    
    static List<KeyFactory> eachKey(LoopIteratorFactory iteratorFactory) {
      List<KeyFactory> keys = new ArrayList<>();
      for (Note root : Note.values()) {
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          String title = "Key of ".concat(keySignature.getKeySignatureString());
          keys.add(new KeyFactory(title, iteratorFactory.iterator(singletonList(keySignature)), 1));
        }
      }
      return keys;
    }

    static List<KeyFactory> someKeys(LoopIteratorFactory iteratorFactory) {
      List<KeyFactory> keys = new ArrayList<>();
      Iterator<Note> noteIterator = iteratorFactory.iterator(List.of(Note.values()));
      for (int i = 0; i < 4; i++) {
        Note root = noteIterator.next();
        for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(root)) {
          String title = "Key of ".concat(keySignature.getKeySignatureString());
          keys.add(new KeyFactory(title, iteratorFactory.iterator(singletonList(keySignature)), 1));
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
      return singletonList(new KeyFactory("Mixed Keys", iteratorFactory.iterator(keys), keys.size()));
    }

    public String getTitle() {
      return title;
    }
  }
  
  public SongFactory(LoopIteratorFactory iteratorFactory, ProgressionFactory progressionFactory, Set<Feature> features) {
    this.features = features;
    
    keyFactories.put(AllKeys, KeyFactory.allKeys(iteratorFactory));
    keyFactories.put(SomeKeys, KeyFactory.someKeys(iteratorFactory));
    keyFactories.put(EachKey, KeyFactory.eachKey(iteratorFactory));
    
    for (ProgressionSet set: progressionFactory.getProgressionSets()) {
      Feature feature = Feature.valueOf(set.getId());
      progressionSets.put(feature, set);
    }
    
  }
  
  public List<SongWrapper> generate(int numberOfBars) {
    List<SongWrapper> songs = new ArrayList<>();
    features.stream().map(keyFactories::get).filter(Objects::nonNull).flatMap(List::stream).forEach(key -> {
      features.stream().map(progressionSets::get).filter(Objects::nonNull).forEach(progressionSet -> {
        progressionSet.getProgressions().stream().forEach(progression -> {
          songs.addAll(play(key, progressionSet, progression, numberOfBars));
        });
      });
    });
    return songs;
  }

  private List<SongWrapper> play(KeyFactory key, ProgressionSet progressionSet, Progression progression, int maxNumberOfBarsInSong) {
    int nuberOfBarsInProgression = progression.getNumberOfBars();
    int numberOfBarsInSong = progressionMustNotSpanSongBoundaries(nuberOfBarsInProgression, maxNumberOfBarsInSong);
    int numberOfProgressionsInSong = numberOfBarsInSong / nuberOfBarsInProgression;
    int numberOfSongs = key.numberOfKeys / numberOfProgressionsInSong;
    if ((key.numberOfKeys % numberOfProgressionsInSong) != 0) {
      numberOfSongs += 1;
    }
    List<SongWrapper> songs = new ArrayList<>();
    for (int i = 0; i < numberOfSongs; i++) {
      List<Bar> bars = new ArrayList<>();
      for (int j = 0; j < numberOfProgressionsInSong; j++) {
        bars.addAll(progression.create(key.getSignatures().next()));
      }
      Song song = new Song(bars);
      if (songsGeneratedSoFar.add(song)) {
        SongWrapper songWrapper = SongWrapper.builder()
          .song(song)
          .key(key.getTitle())
          .mixedKeys(key.getNumberOfKeys() > 1)
          .progressionSet(progressionSet.getId())
          .progression(progression.getTitle())
          .build();
          
        songs.add(songWrapper);
      }
    }
    return songs;
  }


  int progressionMustNotSpanSongBoundaries(int nuberOfBarsInProgression, int numberOfBarsInSong) {
    return numberOfBarsInSong - (numberOfBarsInSong % nuberOfBarsInProgression);
  }

}
