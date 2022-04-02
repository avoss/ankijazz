package com.ankijazz.lily;

import static java.util.stream.Collectors.joining;

import com.ankijazz.rhythm.AbstractRhythm;
import com.ankijazz.rhythm.Event;
import com.ankijazz.rhythm.Quarter;

public class LilyRhythm {

  public enum Type {
    COWBELL("Cowbell", "LilyRhythmCowbell.ly", "cb"), PIANO("Piano", "LilyRhythmPiano.ly", "a");

    private String label;
    private String template;
    private String symbol;

    Type(String label, String template, String symbol) {
      this.label = label;
      this.template = template;
      this.symbol = symbol;
    }
    
    public String getLabel() {
      return label;
    }
    
    public String getTemplate() {
      return template;
    }
    public String getSymbol() {
      return symbol;
    }
  }
  
  private final AbstractRhythm rhythm;
  private final Type type;
  private final int bpm;

  public LilyRhythm(AbstractRhythm rhythm, int bpm, Type type) {
    this.rhythm = rhythm;
    this.bpm = bpm;
    this.type = type;
  }

  public String toLily() {
    return LilyUtil.readTemplate(LilyRhythm.class, type.getTemplate())
        .replace("${scaleNotes}", scaleNotes(Type.PIANO))
        .replace("${cowbellNotes}", scaleNotes(Type.COWBELL))
        .replace("${bpm}", Integer.toString(bpm))
    ;
  }

  public int getBpm() {
    return bpm;
  }

  private CharSequence scaleNotes(Type type) {
    return rhythm.getQuarters().stream().map(q -> toLily(q, type)).collect(joining(" "));
  }
  
  private String toLily(Quarter quarter, Type type) {
    String lily = quarter.getEvents().stream().map(e -> toLily(e, type)).collect(joining(" "));
    if (quarter.isTriplet()) {
      lily = String.format("\\tuplet 3/2 { %s }", lily);
    }
    if (quarter.isTied()) {
      lily = lily + " ~";
    }
    return lily;
  }
  
  private String toLily(Event event, Type type) {
    String symbol = type.getSymbol();
    switch (event) {
    case r1: return "r16";
    case r2: return "r8";
    case r3: return "r8.";
    case r4: return "r4";
    case r6: return "r4.";
    case r8: return "r2";
    case rt: return "r8";
    case b1: return symbol + "16";
    case b2: return symbol + "8";
    case b3: return symbol + "8.";
    case b4: return symbol + "4";
    case bt: return symbol + "8";
    default:
      throw new IllegalArgumentException("Event not supported: " + event);
    }
  }

}
