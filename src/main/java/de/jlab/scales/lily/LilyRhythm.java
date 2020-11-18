package de.jlab.scales.lily;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import de.jlab.scales.Utils;
import de.jlab.scales.rhythm.AbstractRhythm;
import de.jlab.scales.rhythm.Event;
import de.jlab.scales.rhythm.Quarter;

public class LilyRhythm {
  
  private AbstractRhythm rhythm;

  public LilyRhythm(AbstractRhythm rhythm) {
    this.rhythm = rhythm;
  }

  public String toLily() {
    return readTemplate().replace("${scaleNotes}", scaleNotes());
  }

  private CharSequence scaleNotes() {
    return rhythm.getQuarters().stream().map(this::toLily).collect(joining(" "));
  }
  
  private String toLily(Quarter quarter) {
    String lily = quarter.getEvents().stream().map(this::toLily).collect(joining(" "));
    if (quarter.isTriplet()) {
      lily = String.format("\\tuplet 3/2 { %s }", lily);
    }
    if (quarter.isTied()) {
      lily = lily + " ~";
    }
    return lily;
  }
  
  private String toLily(Event event) {
    switch (event) {
    case r1: return "r16";
    case r2: return "r8";
    case r3: return "r8.";
    case r4: return "r4";
    case r6: return "r4.";
    case r8: return "r2";
    case rt: return "r8";
    case b1: return "a16";
    case b2: return "a8";
    case b3: return "a8.";
    case b4: return "a4";
    case bt: return "a8";
    default:
      throw new IllegalArgumentException("Event not supported: " + event);
    }
  }

  private String readTemplate() {
    try (InputStream input = LilyRhythm.class.getResourceAsStream("LilyRhythm.ly")) {
      return Utils.readString(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
