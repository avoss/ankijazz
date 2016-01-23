package de.jlab.scales.random;

import java.util.List;

import org.junit.Assert;

import com.google.common.collect.Lists;

public class TestUtil {

  public static void assertInteger(Sequence<Integer> value, int... expected) {
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals("at index " + i, expected[i], value.next().intValue());
    }
  }

  public static void assertBoolean(Sequence<Boolean> generator, boolean... expected) {
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals("at index " + i, expected[i], generator.next().booleanValue());
    }
  }

  public static void assertBoolean(ContextImpl context, Sequence<Boolean> generator, boolean... expected) {
    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals("at index " + i, expected[i], generator.next().booleanValue());
      context.next();
    }
  }
  
  public static <T> List<T> collectSequence(int size, Sequence<T> sequence) {
    List<T> result = Lists.newArrayList();
    for (int i = 0; i < size; i++)
      result.add(sequence.next());
    return result;
  }

}
