package de.jlab.scales.random;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * parses a String containing numbers 0..10 to a double array containing values in 0..1 (inclusive). 
 * Dot '.' may be used for '0' and 'x' is used for 10 which corresponds to probability 1.0 
 */
public class Patterns {
  private final int MAX_VALUE = 10;
  private Patterns() {}
  
  public static double[] parse(String pattern) {
    return new Patterns().internalParse(pattern);
  }

  private double[] internalParse(String pattern) {
    List<Integer> integers = parseIntegers(pattern);
    return toProbabilityArray(integers);
  }

  private List<Integer> parseIntegers(String pattern) {
    List<Integer> integers = Lists.newArrayList();
    for (char c : pattern.toCharArray()) {
      switch(c) {
      case'0':
      case'1':
      case'2':
      case'3':
      case'4':
      case'5':
      case'6':
      case'7':
      case'8':
      case'9':
        integers.add(c - '0');
        break;
      case'x':
        integers.add(MAX_VALUE);
        break;
      case'.':
        integers.add(0);
        break;
      case'-':
        integers.add(-MAX_VALUE);
        break;
        
      }
    }
    return integers;
  }

  private double[] toProbabilityArray(List<Integer> list) {
    double[] result = new double[list.size()];
    for (int i = 0; i < result.length; i++)
      result[i] = (double)list.get(i) / (double)MAX_VALUE;
    return result;
  }

}
