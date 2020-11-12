package de.jlab.scales.Rhythm;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.rhythm.Event;
import de.jlab.scales.rhythm.Quarters;

public class QuartersTest {

  @Test
  public void testQuarters() {
    List<Event> x = Arrays.asList(Event.values());
    Quarters quarters = new Quarters(4, x);
    
    System.out.println(quarters);
  }

}
