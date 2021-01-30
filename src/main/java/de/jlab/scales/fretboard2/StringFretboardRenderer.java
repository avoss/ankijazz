package de.jlab.scales.fretboard2;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Collections;

public class StringFretboardRenderer implements FretboardRenderer<String> {

  private final Fretboard fretboard;
  private final int minFret;
  private final int maxFret;

  public StringFretboardRenderer(Fretboard fretboard, int minFret, int maxFret) {
    this.fretboard = fretboard;
    this.minFret = minFret;
    this.maxFret = maxFret;
  }

  @Override
  public String render() {
    ArrayList<String> strings = fretboard.getStrings().stream()
        .map(this::render)
        .collect(toCollection(ArrayList::new));
    Collections.reverse(strings);
    return strings.stream().collect(joining("\n"));
  }
  
  class StringMarkerRenderer implements MarkerRenderer {
    StringBuilder sb = new StringBuilder("|");

    @Override
    public void renderEmpty(GuitarString string, int fret) {
      sb.append("---|");
    }

    @Override
    public void renderForeground(GuitarString string, int fret) {
      sb.append("-o-|");
    }

    @Override
    public void renderBackground(GuitarString string, int fret) {
      sb.append("-\u2022-|");
    }
    
    @Override
    public String toString() {
      return sb.toString();
    }

    @Override
    public void renderRoot(GuitarString string, int fret) {
      sb.append("-R-|");
    }
    
  }
  
  String render(GuitarString string) {
    StringMarkerRenderer renderer = new StringMarkerRenderer();
    for (int fret = minFret; fret <= maxFret; fret++) {
      Marker marker = string.markerOf(fret);
      marker.render(renderer, string, fret);
    }
    return renderer.toString();
    
  }
  
}
