package de.jlab.scales.midi.song;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternParserTest {

  class Handler implements PatternParser.Handler {

    StringBuilder sb = new StringBuilder();
    @Override
    public void onValue(double value) {
      sb.append(value == 1.0 ? "x" : (char)(10 * value + '0'));
    }

    @Override
    public void onDash() {
      sb.append("-");
    }

    @Override
    public void onEmpty() {
      sb.append(".");
    }

    @Override
    public void onNext() {
      sb.append(">");
    }
    
    @Override
    public String toString() {
      return sb.toString();
    }
   
  }
  @Test
  public void test() {
    Handler handler = new Handler();
    PatternParser.parse("--.2.x > ..", handler);
    assertEquals("--.2.x>..", handler.toString());
  }

}
