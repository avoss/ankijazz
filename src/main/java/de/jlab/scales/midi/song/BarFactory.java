package de.jlab.scales.midi.song;

import static de.jlab.scales.Utils.randomLoopIterator;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
/*

major 6251 = 2x aeolean/dorian, mixolydian, ionan/lydian
minor 6251 = locrian, dorian, altered, minor
maj-blues = dom, dom, dom 2-5, dom, dim, dom, dom, iim7, V7, I7, vi-ii, V7-I7. Tonika ohne alerierung

Dim7 chord: auf der kleinen Terz löst sich zum grundton auf (maj + dominant - siehe blues), wen kein V-I Bewegung
# https://en.wikipedia.org/wiki/Diminished_seventh_chord#Most_common_functions


ii-5 kombinatorik zu groß, brauche chordfactory zurück - maybe T?


ii7 = Cm7, Cm6, Cm9, Cm11, Cm7b5?
V7 = C7, C9, C13, C7sus4, Abdim7
V7alt = C7b13, C7b9, ..., Edim7 = C7b9, Gb7, Gb9, Gb13
Imaj = C6, CMaj7, CMaj7#11, CMaj9
viim7b5 = Cm7b5, BMaj7

ii7V7alt = ii7 * V7alt, viim7b5 * V7alt 
ii7V7 = ii7(G) * V7, G7sus4 * V7, Ebdim7 * V7

BarFactories
minor7
dominant7
major7
minor7b5

BarFactory.builder(Note root)
  .withMinor7       // Cm7
  .withMinor7Subs   // Cm7, Cm6, Cm9, Cm11, Cm7b5?
  .withDominant
  .withDominantSubs
  .withAlteredSubs
  .withTwoFiveSubs()
  .withTwoFiveAlteredSubs()
  .build();
  
BarFactory.builder(Note root)
  .withDominant()
  .withDominantSubs()
    
 */
//@Builder
public class BarFactory {
  
  static class SeventhBuilder {
    private final BarFactory factory = new BarFactory();
    private final Note root;
    
    SeventhBuilder(Note root) {
      this.root = root;
    }
    
    private List<Scale> transpose(Note root, Scale ...scales) {
      return Arrays.stream(scales).map(scale -> scale.transpose(root)).collect(toCollection(ArrayList::new));
    }
    
    private Iterator<Scale> minor7Subs(Note root) {
      return randomLoopIterator(transpose(root, Cm7, Cm6, Cm9, Cm11, Cm7b5));
    }

    private Iterator<Scale> dominant7Subs(Note root) {
      return randomLoopIterator(transpose(root, C7, C9, C13, C7sus4, Cdim7.transpose(Note.Ab)));
    }
    
    SeventhBuilder withMinor7() {
      factory.add(10, Cm7.transpose(root));
      return this;
    }

    SeventhBuilder withDominant7() {
      factory.add(10, C7.transpose(root));
      return this;
    }

    public SeventhBuilder withAltered() {
      factory.add(10, C7flat9.transpose(root));
      return this;
    }

    
    SeventhBuilder withMajor7() {
      factory.add(10, Cmaj7.transpose(root));
      return this;
    }

    SeventhBuilder withMinor7b5() {
      factory.add(10, Cm7b5.transpose(root));
      return this;
    }

    public SeventhBuilder withTwoFiveAlteredSubs() {
      factory.add(10, List.of(minor7Subs(root.flat9()), dominant7Subs(root.flat5())));
      return this;
    }
    
    BarFactory build() {
      return factory;
    }


  }

  private List<List<Iterator<Scale>>> candidates = new ArrayList<>();
  private Iterator<List<Iterator<Scale>>> iterator;

  public BarFactory add(int probability, Scale scale) {
    return add(probability, List.of(randomLoopIterator(List.of(scale))));
  }
  
  public BarFactory add(int probability, Scale scale1, Scale scale2) {
    add(probability, List.of(
        randomLoopIterator(List.of(scale1)),
        randomLoopIterator(List.of(scale2))
    ));
    return this;
  }
  
  public BarFactory add(int probability, Iterator<Scale> scales) {
    return add(probability, List.of(scales));
  }

  public BarFactory add(int probability, Iterator<Scale> scales1, Iterator<Scale> scales2) {
    return add(probability, List.of(scales1, scales2));
  }
  
  public BarFactory add(int probability, List<Iterator<Scale>> scales) {
    IntStream.range(0, probability).forEach(i -> candidates.add(scales));
    iterator = Utils.randomLoopIterator(candidates);
    return this;
  }

  public Bar next(KeySignature keySignature) {
    int semitones = keySignature.getNotationKey().ordinal();
    List<Chord> chords = new ArrayList<>();
    List<Iterator<Scale>> scales = iterator.next();
    for (Iterator<Scale> iter : scales) {
      Scale scale = iter.next().transpose(semitones);
      ScaleInfo scaleInfo = ScaleUniverse.CHORDS.findFirstOrElseThrow(scale);
      String symbol = String.format("%s%s", keySignature.notate(scale.getRoot()), scaleInfo.getTypeName());
      chords.add(new Chord(scale, symbol));
    }
    return new Bar(chords);
  }
  
  public static SeventhBuilder seventhChordBuilder(Note root) {
    return new SeventhBuilder(root);
  }

}
