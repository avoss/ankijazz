package de.jlab.scales.theory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class DegreeParser {
  
  private final Pattern pattern = Pattern.compile("([b#x]*)([0-9]+)");

  @lombok.Data
  public static class Degree {
    private final int noteIndexInScale;
    private final int offsetToApply;
  }

  public List<Degree> parse(String string) {
    return Arrays.stream(string.split(" +")).map(this::toDegree).collect(Collectors.toList());
  }
  
  private Degree toDegree(String string) {
    Matcher matcher = pattern.matcher(string);
    if (!matcher.find()) {
      throw new IllegalArgumentException("unable to parse " + string);
    }
    int accidentalsToApply = 0;
    for (char c : matcher.group(1).toCharArray()) {
      switch (c) {
      case 'b': accidentalsToApply -= 1; break;
      case '#': accidentalsToApply += 1; break;
      case 'x': accidentalsToApply += 2; break;
      default: throw new IllegalArgumentException("unknown accidental in: " + string);
      }
    }
    int indexInScale = Integer.parseInt(matcher.group(2)) - 1;
    return new Degree(indexInScale, accidentalsToApply);
  }

}
