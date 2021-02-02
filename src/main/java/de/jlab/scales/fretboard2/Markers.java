package de.jlab.scales.fretboard2;

import java.util.function.Function;

import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Markers {
  public static final Marker EMPTY = new Marker() {

    @Override
    public void render(MarkerRenderer renderer, GuitarString string, int fret) {
      renderer.renderEmpty(string, fret);
    }
  };

  private Markers() {
  }

  public static Marker foreground() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderForeground(string, fret);
      }
    };
  }

  public static Marker empty() {
    return EMPTY;
  }

  public static Marker background() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderBackground(string, fret);
      }};
  }

  public static Marker root() {
    return new Marker() {

      @Override
      public void render(MarkerRenderer renderer, GuitarString string, int fret) {
        renderer.renderRoot(string, fret);
      }
      
    };
  }

  public static Function<Note, Marker> marker(Scale scale) {
    return marker(scale.getRoot(), scale);
  }
  
  public static Function<Note, Marker> marker(Note root, Scale foreground) {
    return n -> n == root ? Markers.root() : (foreground.contains(n) ? Markers.foreground() : Markers.background());
  }

}
