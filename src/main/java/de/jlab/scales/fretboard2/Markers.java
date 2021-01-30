package de.jlab.scales.fretboard2;

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

}
