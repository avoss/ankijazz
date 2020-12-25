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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class SongFactory {

  private Set<Feature> features;
  private Map<Feature, ProgressionFactory> progressionFactories;
  private Map<Feature, List<KeyFactory>> keyFactories;

  public enum Feature { Triads, SeventhChords, WithSubstitutions, Major6251, Minor6251, Folk, SimpleBlues, JazzBlues, EachKey, AllKeys }
  
  @Getter
  static class BarFactories {
    private BarFactory cmajor6 = new BarFactory();
    private BarFactory cmajor2 = new BarFactory();
    private BarFactory cmajor5 = new BarFactory();
    private BarFactory cmajor1 = new BarFactory();
    private BarFactory aminor6 = new BarFactory();
    private BarFactory aminor2 = new BarFactory();
    private BarFactory aminor5 = new BarFactory();
    private BarFactory aminor1 = new BarFactory();
    
    BarFactories(Set<Feature> features) {
      // Triad and SeventhChords are about notation, not about what an instrument plays. 
      // e.g. an instrument may play upper structure triads or shell chords even if seventh chords are notated.
      boolean withSubstitutions = features.contains(WithSubstitutions);
      if (features.contains(Triads)) {
        addTriads();
        if (features.contains(WithSubstitutions)) {
          addTriadSubstitutions();
        }
      }
      if (features.contains(SeventhChords)) {
        addSeventhChords();
      }
      // 1. every chord can become a dominant (altered or not)
      // 2. every dominant can become a ii-V
      // 3. lead to any chord with its dominant
      // 4. lead to any chord with the tritone sub of its dominant
//    if (withSubstitutions) {
//      addSeventhChordSubstitutions();
//    }
    }

    private void addTriadSubstitutions() {
      cmajor6.add(1, triad(6+2));
      cmajor2.add(1, triad(2+2));
      cmajor5.add(1, triad(5+2));
      cmajor1.add(1, triad(1+2));
      aminor6.add(1, triad(4+2));
      aminor2.add(1, triad(7+2));
      aminor5.add(2, CmajTriad.transpose(Bb));
      aminor1.add(1, triad(6+2));
    }

    private void addTriads() {
      cmajor6.add(2, triad(6));
      cmajor2.add(2, triad(2));
      cmajor5.add(2, triad(5));
      cmajor1.add(2, triad(1));
      aminor6.add(2, triad(4));
      aminor2.add(2, triad(7));
      aminor5.add(2, CaugTriad.transpose(E));
      aminor1.add(2, triad(6));
    }
    
    private void addSeventhChords() {
      cmajor6.add(2, seventh(6));
      cmajor2.add(2, seventh(2));
      cmajor5.add(2, seventh(5));
      cmajor1.add(2, seventh(1));
      aminor6.add(2, seventh(4));
      aminor2.add(2, seventh(7));
      aminor5.add(2, C7flat9.transpose(E));
      aminor1.add(2, seventh(6));
    }

    private Scale triad(int degree) {
      return CMajor.getChord(degree - 1, 3);
    }
    
    private Scale seventh(int degree) {
      return CMajor.getChord(degree - 1, 4);
    }
  }

  static abstract class ProgressionFactory {
    protected BarFactories factories;
    private int numberOfBars;
    protected ProgressionFactory(BarFactories factories, int numberOfBars) {
      this.factories = factories;
      this.numberOfBars = numberOfBars;
    }
    
    protected abstract List<Bar> create(KeySignature keySignature);
    
    public int getNumberOfBars() {
      return numberOfBars;
    }
  }
  
  // TODO getTitle() contains keysignature.notate(root)
  static class Major6251 extends ProgressionFactory {
    protected Major6251(BarFactories factories) {
      super(factories, 4);
    }
    @Override
    protected List<Bar> create(KeySignature keySignature) {
      return List.of(
          factories.getCmajor6().next(keySignature),
          factories.getCmajor2().next(keySignature),
          factories.getCmajor5().next(keySignature),
          factories.getCmajor1().next(keySignature));
    }
  }

  // TODO getTitle() contains keysignature.notate(root.transpose(-3)) // minor
  static class Minor6251 extends ProgressionFactory {
    protected Minor6251(BarFactories factories) {
      super(factories, 4);
    }
    @Override
    protected List<Bar> create(KeySignature keySignature) {
      return List.of(
          factories.getAminor6().next(keySignature),
          factories.getAminor2().next(keySignature),
          factories.getAminor5().next(keySignature),
          factories.getAminor1().next(keySignature));
    }
  }
  
  static class JazzBlues extends ProgressionFactory {
    protected JazzBlues(BarFactories factories) {
      super(factories, 4);
    }
    @Override
    protected List<Bar> create(KeySignature keySignature) {
      return List.of(
          factories.getAminor6().next(keySignature),
          factories.getAminor2().next(keySignature),
          factories.getAminor5().next(keySignature),
          factories.getAminor1().next(keySignature));
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
    BarFactories barFactories = new BarFactories(features);
    
    progressionFactories = new HashMap<>();
    progressionFactories.put(Major6251, new Major6251(barFactories));
    progressionFactories.put(Minor6251, new Minor6251(barFactories));
    
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
