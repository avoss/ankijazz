package de.jlab.scales.random;

/**
 * converts doubles in range [0..1] to integer range
 * @author win7
 *
 */
public class RangeConverter extends Converter<Integer, Double>{

  private double lowerBound;
  private double upperBound;

  public RangeConverter(Sequence<Double> delegate, int lowerBound, int upperBound) {
    super(delegate);
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  @Override
  protected Integer convert(Double value) {
    return (int) Math.round(lowerBound + value * (upperBound - lowerBound));
  }

}
