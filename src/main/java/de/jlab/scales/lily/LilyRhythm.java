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

  public enum Tempo {
    SLOW(55), MEDIUM(70);

    private int bpm;

    Tempo(int bpm) {
      this.bpm = bpm;
    }
    public int getBpm() {
      return bpm;
    }
  }
  
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
  
  private AbstractRhythm rhythm;
  private Tempo tempo;
  private Type type;

  public LilyRhythm(AbstractRhythm rhythm, Tempo tempo, Type type) {
    this.rhythm = rhythm;
    this.tempo = tempo;
    this.type = type;
  }

  public String toLily() {
    return readTemplate()
        .replace("${scaleNotes}", scaleNotes(Type.PIANO))
        .replace("${cowbellNotes}", scaleNotes(Type.COWBELL))
        .replace("${bpm}", Integer.toString(getBpm()))
    ;
  }

  public int getBpm() {
    return tempo.getBpm();
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

  private String readTemplate() {
    try (InputStream input = LilyRhythm.class.getResourceAsStream(type.getTemplate())) {
      return Utils.readString(input);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
