package com.ankijazz.rhythm;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.rhythm.Quarter;
import com.ankijazz.rhythm.QuarterGenerator;

public class QuarterGeneratorTest {

  @Test
  public void testEventSequences() {
    QuarterGenerator generator = new QuarterGenerator();
    TestUtils.assertFileContentMatches(generator.toString(), getClass(), "QuarterGenerator.txt");
  }
  
  @Test
  public void noDuplicatesAreGenerated() {
    QuarterGenerator generator = new QuarterGenerator();
    List<? extends Quarter> list = generator.getQuarters().stream().collect(Collectors.toList());
    Set<? extends Quarter> set = list.stream().collect(Collectors.toSet());
    assertEquals(list.size(), set.size());
    
  }

}
