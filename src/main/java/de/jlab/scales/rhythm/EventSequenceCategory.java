package de.jlab.scales.rhythm;

import java.util.List;

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
}
