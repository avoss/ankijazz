package de.jlab.scales.lily;

@lombok.RequiredArgsConstructor
@lombok.Getter
public class LilyClef {
  public static final LilyClef TREBLE = new LilyClef("treble", "e''", "e'");
  public static final LilyClef BASS = new LilyClef("bass", "g", "g,");
  private final String clef;
  private final String descendingRelativeTo;
  private final String ascendingRelativeTo;
}
