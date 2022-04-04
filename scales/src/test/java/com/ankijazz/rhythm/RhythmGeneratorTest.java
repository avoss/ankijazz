package com.ankijazz.rhythm;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.assertj.core.data.Offset;
import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.Utils;

public class RhythmGeneratorTest {

  @Test
  public void testRhythmGenerator() {
    RhythmGenerator generator = new RhythmGenerator(Utils.fixedLoopIteratorFactory());
    List<String> actual = generator.generate().stream()
        .map( r -> format("%30s : %s", r.getTitle(), r.toString()))
        .collect(Collectors.toList());
    TestUtils.assertFileContentMatches(actual, RhythmGeneratorTest.class, "RhythmGenerator.txt");
  }

  @Test
  public void assertNoDuplicatesAreGenerated() {
    RhythmGenerator generator = new RhythmGenerator(Utils.randomLoopIteratorFactory());
    List<List<Quarter>> list = generator.generate().stream().map(r -> r.getQuarters()).collect(Collectors.toList());
    Set<List<Quarter>> set = list.stream().collect(Collectors.toSet());
    // TODO too many duplicates in random mode - maybe create permutations systematically? 
    assertThat(set.size()).isCloseTo(list.size(), Offset.offset(10));
  }
  
  @Test
  public void assertThatLastQuarterIsNotTied() {
    // TODO it's not just last quarter, it is every quarter within the loop
    RhythmGenerator generator = new RhythmGenerator(Utils.fixedLoopIteratorFactory());
    Predicate<AbstractRhythm> filter = r -> {
      Quarter first = Utils.getFirst(r.getQuarters());
      Quarter last = Utils.getLast(r.getQuarters());
      return !first.startsWithBeat() && last.isTied();
    };
    Optional<AbstractRhythm> rhythm = generator.generate().stream().filter(filter).findAny();
    assertFalse(rhythm.isPresent());
    
  }
}
