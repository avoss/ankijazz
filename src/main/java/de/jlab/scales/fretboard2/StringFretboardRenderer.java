package de.jlab.scales.fretboard2;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringFretboardRenderer implements FretboardRenderer<List<String>> {

  private final Fretboard fretboard;

  public StringFretboardRenderer(Fretboard fretboard) {
    this.fretboard = fretboard;
  }

  @Override
  public List<String> render() {
    ArrayList<String> strings = fretboard.getStrings().stream()
        .map(this::render)
        .collect(toCollection(ArrayList::new));
    Collections.reverse(strings);
    return strings;
  }
  
  @Override
  public String toString() {
    return render().stream().collect(joining("\n"));
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
    public void renderRoot(GuitarString string, int fret) {
      sb.append("-R-|");
    }

    @Override
    public String toString() {
      return sb.toString();
    }
    
  }
  
  public String render(GuitarString string) {
    return render(string, fretboard.getMinFret(), fretboard.getMaxFret());
  }
  
  public String render(GuitarString string, int minFret, int maxFret) {
    StringMarkerRenderer renderer = new StringMarkerRenderer();
    for (int fret = minFret; fret <= maxFret; fret++) {
      Marker marker = string.markerOf(fret);
      marker.render(renderer, string, fret);
    }
    return renderer.toString();
    
  }
  
}
