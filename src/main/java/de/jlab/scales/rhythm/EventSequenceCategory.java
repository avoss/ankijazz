package de.jlab.scales.rhythm;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.fraction.Fraction;

/**
 * Two EventSequences belong to the same category, if they are equal ignoring the length of the notes
 */
@lombok.EqualsAndHashCode
@lombok.RequiredArgsConstructor
@lombok.Getter
@lombok.ToString
public class EventSequenceCategory {
  private final List<Fraction> beatPositions;

  public static EventSequenceCategory of(int ... beatPositions) {
    List<Fraction> fractions = IntStream.of(beatPositions).mapToObj(position -> new Fraction(position)).collect(toList());
    return new EventSequenceCategory(fractions);
  }
}
