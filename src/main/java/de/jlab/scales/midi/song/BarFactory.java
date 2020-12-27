package de.jlab.scales.midi.song;

import static de.jlab.scales.theory.Scales.C13;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat5;
import static de.jlab.scales.theory.Scales.C7flat5flat9;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C7sharp5sharp9;
import static de.jlab.scales.theory.Scales.C7sharp9;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.C9;
import static de.jlab.scales.theory.Scales.Cm11;
import static de.jlab.scales.theory.Scales.Cm6;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.*;
import static de.jlab.scales.theory.Scales.Cm9;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp11;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp5;
import static de.jlab.scales.theory.Scales.Cmmaj7;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;

public class BarFactory {
  
  public static interface Builder {
    Builder withRoot(Note root);
    Builder withMinor7();
    Builder withMinor7Subs();
    Builder withDominant7();
    Builder withDominant7Subs();
    Builder withAltered();
    Builder withAlteredSubs();
    Builder withMajor7();
    Builder withMajor7Subs();
    Builder withMinor7b5();
    Builder withMinor7b5Subs();
    Builder withTwoFiveSubs();
    Builder withTwoFiveAlteredSubs();
    BarFactory build();
  }
  
  private static Iterator<Scale> iterator(Note root, Scale ...scales) {
    return randomLoopIterator(Arrays.stream(scales).map(scale -> scale.transpose(root)).collect(toList()));
  }

  private static Iterator<Scale> randomLoopIterator(List<Scale> scales) {
    for (Scale scale: scales) {
      if (scale.getNumberOfNotes() < 3) {
        throw new IllegalStateException("cannot have chord with < 3 notes (won't be found in scale universe): " + scale);
      }
    }
    return Utils.randomLoopIterator(scales);
  }

  static class SeventhBuilder implements Builder {
    private final BarFactory factory = new BarFactory();
    private Note root = Note.C;

    @Override
    public Builder withRoot(Note root) {
      this.root = root;
      return this;
    }
    
    @Override
    public Builder withMinor7() {
      factory.add(1, Cm7.transpose(root));
      return this;
    }

    @Override
    public Builder withMinor7Subs() {
      factory.add(5, iterator(root, Cm6, Cm9, Cm11, Cm7b5, Cmmaj7));
      return this;
    }
    
    @Override
    public Builder withDominant7() {
      factory.add(1, C7.transpose(root));
      return this;
    }

    @Override
    public Builder withDominant7Subs() {
      factory.add(3, iterator(root, C9, C13, C7sus4));
      return this;
    }

    @Override
    public Builder withAltered() {
      factory.add(1, C7flat9.transpose(root));
      return this;
    }

    @Override
    public Builder withAlteredSubs() {
      List<Scale> list = Stream.concat(
        Stream.of(C7sharp9, C7flat5, C7sharp5, C7sharp5sharp9, C7flat5flat9).map(s -> s.transpose(root)),
        Stream.of(C7, C9, C13).map(s -> s.transpose(root.flat5()))
      ).collect(toList());
      factory.add(8, randomLoopIterator(list));
      return this;
    }

    @Override
    public Builder withMajor7() {
      factory.add(1, Cmaj7.transpose(root));
      return this;
    }

    @Override
    public Builder withMajor7Subs() {
      factory.add(2, iterator(root, Cmaj7Sharp5, Cmaj7Sharp11));
      return this;
    }
    
    @Override
    public Builder withMinor7b5() {
      factory.add(1, Cm7b5.transpose(root));
      return this;
    }
    
    @Override
    public Builder withMinor7b5Subs() {
      factory.add(1, iterator(root, Cmaj7.transpose(-1)));
      return this;
    }

    @Override
    public Builder withTwoFiveSubs() {
      factory.add(4, List.of(iterator(root.five(), Cm7, Cm6, Cm9, Cm7b5), iterator(root, C7, C9, C13, C7sus4)));
      return this;
    }
    
    @Override
    public Builder withTwoFiveAlteredSubs() {
      factory.add(4, List.of(iterator(root.flat9(), Cm7, Cm6, Cm9, Cm7b5), iterator(root.flat5(), C7, C9, C13)));
      return this;
    }
    
    @Override
    public BarFactory build() {
      return factory;
    }

  }
  
  public static class TriadsBuilder implements Builder {
    private final BarFactory factory = new BarFactory();
    private Note root = Note.C;

    @Override
    public Builder withRoot(Note root) {
      this.root = root;
      return this;
    }
    
    @Override
    public Builder withMinor7() {
      factory.add(1, lower(root, Cm7));
      return this;
    }


    @Override
    public Builder withMinor7Subs() {
      factory.add(1, upper(root, Cm7));
      return this;
    }
    
    @Override
    public Builder withDominant7() {
      factory.add(1, lower(root, C7));
      return this;
    }

    @Override
    public Builder withDominant7Subs() {
      factory.add(1, upper(root, C7));
      return this;
    }

    @Override
    public Builder withAltered() {
      factory.add(1, CaugTriad.transpose(root));
      return this;
    }

    @Override
    public Builder withAlteredSubs() {
      factory.add(1, CmajTriad.transpose(root.flat5()));
      return this;
    }

    @Override
    public Builder withMajor7() {
      factory.add(1, lower(root, Cmaj7));
      return this;
    }

    @Override
    public Builder withMajor7Subs() {
      factory.add(1, upper(root, Cmaj7));
      return this;
    }
    
    @Override
    public Builder withMinor7b5() {
      factory.add(1, lower(root, Cm7b5));
      return this;
    }
    
    @Override
    public Builder withMinor7b5Subs() {
      factory.add(1, upper(root, Cm7b5));
      return this;
    }

    private Iterator<Scale> minor7subs(Note root) {
      return iterator(root, lower(Cm7), upper(Cm7), lower(Cm7b5), upper(Cm7b5));
    }
    
    @Override
    public Builder withTwoFiveSubs() {
      factory.add(5, List.of(minor7subs(root.five()), iterator(root, lower(C7), upper(C7))));
      return this;
    }
    
    @Override
    public Builder withTwoFiveAlteredSubs() {
      factory.add(4, List.of(minor7subs(root.flat9()), iterator(root.flat5(), lower(C7), upper(C7))));
      return this;
    }
    
    @Override
    public BarFactory build() {
      return factory;
    }

    private Scale lower(Note root, Scale scale) {
      return lower(scale.transpose(root));
    }

    private Scale lower(Scale scale) {
      return new Scale(scale.getNote(0), scale.getNote(1), scale.getNote(2));
    }
    
    private Scale upper(Note root, Scale scale) {
      return upper(scale.transpose(root));
    }

    private Scale upper(Scale scale) {
      return new Scale(scale.getNote(1), scale.getNote(2), scale.getNote(3));
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
  
  public static Builder seventhChordBuilder() {
    return new SeventhBuilder();
  }

  public static Builder triadsBuilder() {
    return new TriadsBuilder();
  }

}
