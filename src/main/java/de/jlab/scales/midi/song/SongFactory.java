package de.jlab.scales.midi.song;

import static de.jlab.scales.Utils.loopIterator;
import static de.jlab.scales.Utils.randomLoopIterator;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Major6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.Minor6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.SeventhChords;
import static de.jlab.scales.midi.song.SongFactory.Feature.Triads;
import static de.jlab.scales.midi.song.SongFactory.Feature.WithSubstitutions;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.CdimTriad;
import static de.jlab.scales.theory.Scales.CmajTriad;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class SongFactory {

  private Set<Feature> features;
  private ChordFactories chordFactories;
  private Map<Feature, ProgressionFactory> progressionFactories;
  private Map<Feature, List<KeyFactory>> keyFactories;

  public enum Feature { Triads, SeventhChords, WithSubstitutions, Major6251, Minor6251, Folk, SimpleBlues, JazzBlues, EachKey, AllKeys }
  
  @Getter
  static class ChordFactories {
    private ChordFactory cmajor6 = new ChordFactory();
    private ChordFactory cmajor2 = new ChordFactory();
    private ChordFactory cmajor5 = new ChordFactory();
    private ChordFactory cmajor1 = new ChordFactory();
    private ChordFactory aminor6 = new ChordFactory();
    private ChordFactory aminor2 = new ChordFactory();
    private ChordFactory aminor5 = new ChordFactory();
    private ChordFactory aminor1 = new ChordFactory();
    
    ChordFactories(Set<Feature> features) {
      boolean withSubstitutions = features.contains(WithSubstitutions);
      if (features.contains(Triads)) {
        addTriads();
        if (withSubstitutions) {
          addTriadSubstitutions();
        }
      }
      if (features.contains(SeventhChords)) {
        addSeventhChords();
//        if (withSubstitutions) {
//          addSeventhChordSubstitutions();
//        }
      }
    }

    private void addTriads() {
      cmajor6.add(triad(6), 2);
      cmajor2.add(triad(2), 2);
      cmajor5.add(triad(5), 2);
      cmajor1.add(triad(1), 2);
      aminor6.add(triad(4), 2);
      aminor2.add(triad(7), 2);
      aminor5.add(CaugTriad.transpose(E), 2);
      aminor1.add(triad(6), 2);
    }
    
    private void addTriadSubstitutions() {
      cmajor6.add(triad(6+2), 1);
      cmajor6.add(triad(6-2), 1);
      cmajor2.add(triad(2+2), 1);
      cmajor2.add(triad(2-2), 1);
      cmajor5.add(triad(5+2), 1);
      cmajor5.add(triad(5-2), 1);
      cmajor1.add(triad(1+2), 1);
      cmajor1.add(triad(1-2), 1);
      aminor6.add(triad(4+2), 1);
      aminor6.add(triad(4-2), 1);
      aminor2.add(triad(7+2), 1);
      aminor2.add(triad(7-2), 1);
      aminor5.add(CdimTriad.transpose(B), 1);
      aminor5.add(CdimTriad.transpose(D), 1);
      aminor5.add(CmajTriad.transpose(Bb), 2);
      aminor1.add(triad(6+2), 1);
      aminor1.add(triad(6-2), 1);
    }

    private void addSeventhChords() {
      cmajor6.add(seventh(6), 2);
      cmajor2.add(seventh(2), 2);
      cmajor5.add(seventh(5), 2);
      cmajor1.add(seventh(1), 2);
      aminor6.add(seventh(4), 2);
      aminor2.add(seventh(7), 2);
      aminor5.add(C7flat9.transpose(E), 2);
      aminor1.add(seventh(6), 2);
    }
    
    private Scale triad(int degree) {
      return Scales.CMajor.getChord(degree - 1, 3);
    }
    
    private Scale seventh(int degree) {
      return Scales.CMajor.getChord(degree - 1, 4);
    }
  }

  static abstract class ProgressionFactory {
    protected ChordFactories factories;
    private int numberOfBars;
    protected ProgressionFactory(ChordFactories factories, int numberOfBars) {
      this.factories = factories;
      this.numberOfBars = numberOfBars;
    }
    
    public List<Bar> create(KeySignature keySignature) {
      int semitones = keySignature.getNotationKey().ordinal();
      return create(semitones, keySignature).map(chord -> Bar.of(chord)).collect(toList());
    }
    
    protected abstract Stream<Chord> create(int semitones, KeySignature keySignature);
    
    public int getNumberOfBars() {
      return numberOfBars;
    }
  }
  
  // TODO getTitle() contains keysignature.notate(root)
  static class Major6251 extends ProgressionFactory {
    protected Major6251(ChordFactories factories) {
      super(factories, 4);
    }
    @Override
    protected Stream<Chord> create(int semitones, KeySignature keySignature) {
      return Stream.of(
          factories.getCmajor6().next(semitones, keySignature),
          factories.getCmajor2().next(semitones, keySignature),
          factories.getCmajor5().next(semitones, keySignature),
          factories.getCmajor1().next(semitones, keySignature));
    }
  }

  // TODO getTitle() contains keysignature.notate(root.transpose(-3)) // minor
  static class Minor6251 extends ProgressionFactory {
    protected Minor6251(ChordFactories factories) {
      super(factories, 4);
    }
    @Override
    protected Stream<Chord> create(int semitones, KeySignature keySignature) {
      return Stream.of(
          factories.getAminor6().next(semitones, keySignature),
          factories.getAminor2().next(semitones, keySignature),
          factories.getAminor5().next(semitones, keySignature),
          factories.getAminor1().next(semitones, keySignature));
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
    chordFactories = new ChordFactories(features);
    
    progressionFactories = new HashMap<>();
    progressionFactories.put(Major6251, new Major6251(chordFactories));
    progressionFactories.put(Minor6251, new Minor6251(chordFactories));
    
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
