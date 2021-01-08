package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.SomeKeys;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.midi.song.ProgressionFactory.Progression;
import de.jlab.scales.midi.song.ProgressionFactory.ProgressionSet;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import lombok.Data;

public class SongFactory {
  private Set<Feature> features;
  private Map<Feature, List<? extends KeyFactory>> keyFactories = new HashMap<>();
  private Map<Feature, ProgressionSet> progressionSets = new HashMap<>();
  private Set<Song> songsGeneratedSoFar = new HashSet<>();
  private LoopIteratorFactory iteratorFactory;

  public enum Feature { Test, Workouts, Triads, TwoFiveOnes, ExtTwoFiveOnes, SimpleBlues, JazzBlues, EachKey, SomeKeys, AllKeys }

  interface KeyFactory {
    String getTitle();
    Iterator<KeySignature> getSignatures();
    int getNumberOfKeys();
    void nextSong(boolean minor);
  }
  
  @Data
  static class SimpleKeyFactory implements KeyFactory {
    private final String title;
    private final Iterator<KeySignature> signatures;
    private final int numberOfKeys;
    
    @Override
    public void nextSong(boolean minor) {
    }
  }

  List<KeySignature> allKeySignatures() {
    return Arrays.stream(Note.values()).flatMap(root ->  BuiltinScaleType.Major.getKeySignatures(root).stream()).collect(Collectors.toList());
  }
  
  List<? extends KeyFactory> allKeys() {
    List<KeySignature> keys = allKeySignatures();
    return singletonList(new SimpleKeyFactory("Mixed Keys", iteratorFactory.iterator(keys), keys.size()));
  }
  
  List<? extends KeyFactory> eachKey() {
    return someKeys(allKeySignatures().size());
  }

  List<? extends KeyFactory> someKeys(int numberOfKeys) {
    Iterator<KeySignature> keySignatures = iteratorFactory.iterator(allKeySignatures());
    List<KeyFactory> factories = new ArrayList<>();
    IntStream.range(0, numberOfKeys).forEach(i -> {
      factories.add(new KeyFactory() {
        String title;
        KeySignature keySignature;
        
        @Override
        public String getTitle() {
          return title;
        }
        
        @Override
        public Iterator<KeySignature> getSignatures() {
          return iteratorFactory.iterator(List.of(keySignature));
        }
        
        @Override
        public int getNumberOfKeys() {
          return 1;
        }
        
        @Override
        public void nextSong(boolean minor) {
          keySignature = keySignatures.next();
          Note root = keySignature.getNotationKey();
          final String format = "Key of %s %s";
          if (minor) {
            title = format(format, keySignature.notate(root.major6()), "Minor");
          } else {
            title = format(format, keySignature.notate(root), "Major");
          }
        }
        
      });
    });
    return factories;
  }
  
  
  public SongFactory(LoopIteratorFactory iteratorFactory, ProgressionFactory progressionFactory, Set<Feature> features) {
    this.iteratorFactory = iteratorFactory;
    this.features = features;
    
    keyFactories.put(AllKeys, allKeys());
    keyFactories.put(SomeKeys, someKeys(4));
    keyFactories.put(EachKey, eachKey());
    
    for (ProgressionSet set: progressionFactory.getProgressionSets()) {
      Feature feature = Feature.valueOf(set.getId());
      progressionSets.put(feature, set);
    }
    
  }
  
  public List<SongWrapper> generate(int numberOfBars) {
    List<SongWrapper> songs = new ArrayList<>();
    features.stream().map(progressionSets::get).filter(Objects::nonNull).forEach(progressionSet -> {
      progressionSet.getProgressions().stream().forEach(progression -> {
        features.stream().map(keyFactories::get).filter(Objects::nonNull).flatMap(List::stream).forEach(key -> {
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
    int numberOfSongs = key.getNumberOfKeys() / numberOfProgressionsInSong;
    if ((key.getNumberOfKeys() % numberOfProgressionsInSong) != 0) {
      numberOfSongs += 1;
    }
    List<SongWrapper> songs = new ArrayList<>();
    for (int i = 0; i < numberOfSongs; i++) {
      key.nextSong(progression.isMinor());
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
          .progressionSet(progressionSet.getTitle())
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
