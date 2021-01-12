package de.jlab.scales.rhythm;

import static de.jlab.scales.Utils.repeat;
import static de.jlab.scales.rhythm.BasicRhythm.Type.BASIC;
import static de.jlab.scales.rhythm.BasicRhythm.Type.SYNCOPATED;
import static de.jlab.scales.rhythm.BasicRhythm.Type.TIED;
import static de.jlab.scales.rhythm.Event.b1;
import static de.jlab.scales.rhythm.Event.b2;
import static de.jlab.scales.rhythm.Event.b3;
import static de.jlab.scales.rhythm.Event.b4;
import static de.jlab.scales.rhythm.Event.bt;
import static de.jlab.scales.rhythm.Event.r1;
import static de.jlab.scales.rhythm.Event.r2;
import static de.jlab.scales.rhythm.Event.r3;
import static de.jlab.scales.rhythm.Event.r4;
import static de.jlab.scales.rhythm.Event.rt;
import static de.jlab.scales.rhythm.Quarter.q;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.Utils.LoopIteratorFactory;

// FIXME RhythmGenerator has no tests
public class RhythmGenerator {

  private static class RandomTies implements Function<List<Quarter>, List<Quarter>> {

    Iterator<Boolean> random;
    RandomTies(LoopIteratorFactory iteratorFactory) {
      this.random = iteratorFactory.iterator(List.of(TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE));
    }
    
    @Override
    public List<Quarter> apply(List<Quarter> quarters) {
      List<Quarter> copy = new ArrayList<>();
      for (int i = 1; i < quarters.size(); i++) {
        Quarter prev = quarters.get(i-1);
        Quarter next = quarters.get(i);
        copy.add(canCreateTie(prev, next) && shouldCreateTie(prev, next) ? prev.tie() : prev);
      }
      copy.add(quarters.get(quarters.size()-1));
      return copy;
    }
    
    boolean canCreateTie(Quarter prev, Quarter next) {
      if (prev.getNumberOfEvents() + next.getNumberOfEvents() < 3) {
        return false;
      }
      return prev.endsWithBeat() && next.startsWithBeat();
    }
    
    boolean shouldCreateTie(Quarter prev, Quarter next) {
      return random.next();
    }
    
  }
  
  private final int numberOfRhythms = 200;
  private final int numberOfQuarters = 16;
  private final Iterator<Quarter> quarterIterator;
  
  private final RandomTies randomTies;
  private final LoopIteratorFactory iteratorFactory;

  public RhythmGenerator(LoopIteratorFactory iteratorFactory) {
    this(iteratorFactory, new QuarterGenerator().getQuarters());
  }

  public RhythmGenerator(LoopIteratorFactory iteratorFactory, Collection<? extends Quarter> quarters) {
    this.iteratorFactory = iteratorFactory;
    this.quarterIterator = iteratorFactory.iterator(quarters);
    this.randomTies = new RandomTies(iteratorFactory);
  }
  

  
  public List<AbstractRhythm> generate() {
    List<AbstractRhythm> result = new ArrayList<>();
//    result.addAll(allBlocks());
    result.addAll(basicRhythms());
    result.addAll(standardRhythms());
    result.addAll(group3Rhythms());
    result.addAll(group5Rhythms());
    result.addAll(randomRhythms(result.size()));
    return result;
  }


  private Collection<? extends AbstractRhythm> allBlocks() {
    BasicRhythm basic = new BasicRhythm(BASIC, List.of(
        q(b4),
        q(b2, b2),
        q(b3, b1),
        q(b1, b3),
        q(b2, b1, b1),
        q(b1, b1, b2),
        q(b1, b2, b1),
        q(b1, b1, b1, b1),
        q(bt, bt, bt)));

    BasicRhythm syncopated = new BasicRhythm(SYNCOPATED, List.of(
        q(r4),
        q(r2, b2),
        q(r3, b1),
        q(r1, b3),
        q(r2, b1, b1),
        q(r1, b1, b2),
        q(r1, b2, b1),
        q(r1, b1, b1, b1),
        q(rt, bt, bt)));
    
    BasicRhythm tied = new BasicRhythm(TIED, List.of(
        q(b4),
        q(b2, b2).tie(),
        q(b3, b1).tie(),
        q(b1, b3).tie(),
        q(b2, b1, b1).tie(),
        q(b1, b1, b2).tie(),
        q(b1, b2, b1).tie(),
        q(b1, b1, b1, b1).tie(),
        q(bt, bt, bt).tie()));
    
    return List.of(basic, syncopated, tied);
  }
  
  private Collection<? extends AbstractRhythm> basicRhythms() {
    List<AbstractRhythm> result = new ArrayList<>();
    result.add(new BasicRhythm(BASIC, repeat(16, q(b2, b2))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b1, b1, b1, b1))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b1, b1, b2))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b2, b1, b1))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b1, b2, b1))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b1, b3))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(b3, b1))));
    result.add(new BasicRhythm(BASIC, repeat(16, q(bt, bt, bt))));
    
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r2, b2))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r1, b1, b1, b1))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r1, b1, b2))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r2, b1, b1))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r1, b2, b1))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r1, b3))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(r3, b1))));
    result.add(new BasicRhythm(SYNCOPATED, repeat(16, q(rt, bt, bt))));

    result.add(new BasicRhythm(TIED, repeat(16, q(b2, b2).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b1, b1, b1, b1).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b1, b1, b2).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b2, b1, b1).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b1, b2, b1).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b1, b3).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(b3, b1).tie())));
    result.add(new BasicRhythm(TIED, repeat(16, q(bt, bt, bt).tie())));
    
    return result;
  }


  private Collection<? extends AbstractRhythm> group3Rhythms() {
    Quarter q1 = q(b3, b1).tie();
    Quarter q2 = q(b2, b2).tie();
    Quarter q3 = q(b1, b3);
    Quarter q4 = q(b3, b1);
    return transpose(new GroupingRhythm(3, repeat(4, q1, q2, q3, q4)));
  }

  private Collection<? extends AbstractRhythm> group5Rhythms() {
    Quarter q1 = q(b2, b2).tie();
    Quarter q2 = q(b1, b2, b1).tie();
    Quarter q3 = q(b2, b2);
    Quarter q4 = q(b3, r1);
    return transpose(new GroupingRhythm(5, repeat(4, q1, q2, q3, q4)));
  }

  private Collection<? extends AbstractRhythm> transpose(GroupingRhythm rhythm) {
    List<AbstractRhythm> result = new ArrayList<>();
    result.add(rhythm);
    for (int i = 1; i < 16; i++) {
      result.add(rhythm.transpose(i));
    }
    return result;
  }
  
  private Collection<? extends AbstractRhythm> randomRhythms(int rhythmsSoFar) {
    List<AbstractRhythm> result = new ArrayList<>();
    int numberOfRhythmsToCreate = numberOfRhythms - rhythmsSoFar;
    Interpolator uniqueQuartersInterpolator = Utils.interpolator(0, numberOfRhythmsToCreate, 2, 5);
    for (int i = 0; i < numberOfRhythmsToCreate; i++) {
      int numberOfUniqueQuarters = uniqueQuartersInterpolator.apply(i);
      List<Quarter> quarters = chooseQuarters(numberOfUniqueQuarters);
      result.add(new RandomRhythm(quarters));
    }
    return result;
  }


  private List<Quarter> chooseQuarters(int numberOfUniqueQuarters) {
    List<Quarter> uniqueQuarters = take(numberOfUniqueQuarters, quarterIterator);
    List<Quarter> tiedQuarters = randomTies.apply(uniqueQuarters);
    List<Quarter> quarters = take(numberOfQuarters, Utils.loopIterator(tiedQuarters));
    return ensureTiesAreConsistent(quarters);
  }

  private List<Quarter> ensureTiesAreConsistent(List<Quarter> quarters) {
    if (quarters.get(0).startsWithBeat()) {
      return quarters;
    }
    Quarter last = Utils.getLast(quarters);
    if (last.isTied()) {
      quarters.remove(quarters.size()-1);
      quarters.add(last.unTie());
    }
    return quarters;
  }

  private List<Quarter> take(int numberOfElements, Iterator<Quarter> iterator) {
    List<Quarter> result = new ArrayList<>();
    for (int i = 0; i < numberOfElements; i++) {
      result.add(iterator.next());
    }
    return result;
  }
    
  private Collection<? extends AbstractRhythm> standardRhythms() {
    List<AbstractRhythm> result = new ArrayList<>();
    result.add(shuffle());
    result.addAll(claves());
    result.add(charleston());
    result.addAll(bossas());
    result.add(choro());
    result.add(baiao());
    result.addAll(montunos());
    result.addAll(cascara());
    return result;
  }

  private StandardRhythm shuffle() {
    return new StandardRhythm("Shuffle", repeat(16, q(bt, rt, bt)));
  }
  
  private Collection<? extends AbstractRhythm> cascara() {
    Quarter q1 = q(b2, b1, b1);
    Quarter q2 = q(r1, b2, b1);
    Quarter q3 = q(b2, b2);
    Quarter q4 = q(b1, b3);
    return List.of(
        new StandardRhythm("3/2 Cascara", repeat(4, q1, q2, q3, q4)),
        new StandardRhythm("2/3 Cascara", repeat(4, q3, q4, q1, q2))
        );
  }

  private Collection<? extends AbstractRhythm> montunos() {
    Quarter q1 = q(b2, b1, b1);
    Quarter q2 = q(r1, b2, b1);
    return List.of(
        new StandardRhythm("2/3 Montuno", repeat(4, q1, q2, q2, q2)),
        new StandardRhythm("3/2 Montuno", repeat(4, q2, q2, q1, q2))
      );
  }

  private StandardRhythm baiao() {
    Quarter q1 = q(b3, b1).tie();
    Quarter q2 = q(b2, b2);
    return new StandardRhythm("Baiao", repeat(8, q1, q2));
  }

  private StandardRhythm choro() {
    Quarter q1 = q(r2, b2).tie();
    Quarter q2 = q(b2, b1, r1);
    return new StandardRhythm("Choro Variation", repeat(8, q1, q2));
  }

  private Collection<? extends AbstractRhythm> bossas() {
    Quarter q1 = q(b2, b2);
    Quarter q2 = q(r1, b1, r1, b1);
    return List.of(
          new StandardRhythm("Bossa Nova", repeat(8, q1, q2)),
          new StandardRhythm("Reverse Bossa Nova", repeat(8, q2, q1)),
          new StandardRhythm("Samba", repeat(4, q2, q1, q1, q2)),
          new StandardRhythm("Reverse Samba", repeat(4, q1, q2, q2, q1))
        );
  }

  private StandardRhythm charleston() {
    return new StandardRhythm("Carleston", repeat(8, q(b3, b1), q(r4)));
  }

  private Collection<? extends AbstractRhythm> claves() {
    Collection<AbstractRhythm> result = new ArrayList<>();
    Quarter q1 = q(r2, b2);
    Quarter q2 = q(b2, r2);
    Quarter q3 = q(b3, b1).tie();
    Quarter q4 = q(b2, b2);
    Quarter q2bossa = q(r1, b2, r1);
    Quarter q4rhumba = q(b3, b1);
    result.add(new StandardRhythm("2-3 Son Clave", repeat(4, q1, q2, q3, q4)));
    result.add(new StandardRhythm("3-2 Son Clave", repeat(4, q3, q4, q1, q2)));
    result.add(new StandardRhythm("2-3 Rhumba Clave", repeat(4, q1, q2, q3, q4rhumba)));
    result.add(new StandardRhythm("3-2 Rhumba Clave", repeat(4, q3, q4rhumba, q1, q2)));
    result.add(new StandardRhythm("2-3 Bossa Clave", repeat(4, q1, q2bossa, q3, q4)));
    result.add(new StandardRhythm("3-2 Bossa Clave", repeat(4, q3, q4, q1, q2bossa)));
    return result;
  }


}
