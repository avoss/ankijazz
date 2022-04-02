package com.ankijazz.fretboard;

import static com.ankijazz.theory.Scales.Cmaj7;

import java.util.function.Function;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.fretboard.Marker;
import com.ankijazz.fretboard.MidiFretboardRenderer;
import com.ankijazz.fretboard.NPS;
import com.ankijazz.fretboard.Position;
import com.ankijazz.fretboard.MidiFretboardRenderer.MidiFretboardRendererBuilder;
import com.ankijazz.midi.Part;
import com.ankijazz.theory.Note;
public class MidiFretboardRendererTest {

  @Test
  public void testForegroundAndBackground() {
    Part part = builder(Marker.outline(Cmaj7))
      .renderForeground(true)
      .renderBackground(true)
      .foregroundIncludesRoot(true)
      .build()
      .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackground.midi");
  }

  private MidiFretboardRendererBuilder builder(Function<Note, Marker> markers) {
    Position position = NPS.C_MAJOR_CAGED.transpose(Note.G).getPositions().get(1);
    Fretboard fretboard = new Fretboard(position, markers);
    return MidiFretboardRenderer.builder()
      .fretboard(fretboard)
      .backgroundChord(Cmaj7);
  }

  @Test
  public void testForegroundOnly() {
    Part part = builder((n) -> Marker.FOREGROUND)
        .renderForeground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundOnly.midi");
  }

  @Test
  public void testBackgroundOnly() {
    Part part = builder((n) -> Marker.BACKGROUND)
        .renderBackground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "BackgroundOnly.midi");
  }

  @Test
  public void testForegroundOnlyNoRoot() {
    Part part = builder(Marker.outline(Cmaj7))
        .renderForeground(true)
        .renderBackground(true)
        .build()
        .render();
    TestUtils.assertMidiMatches(part, getClass(), "ForegroundAndBackgroundNoRoot.midi");
  }
  
}
