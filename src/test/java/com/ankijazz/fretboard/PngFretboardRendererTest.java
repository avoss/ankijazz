package com.ankijazz.fretboard;

import static com.ankijazz.fretboard.BoxMarker.BoxPosition.RIGHT;
import static com.ankijazz.fretboard.StandardTuning.HIGH_E_STRING;

import java.awt.image.BufferedImage;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.fretboard.Fretboard;
import com.ankijazz.fretboard.Marker;
import com.ankijazz.fretboard.NPS;
import com.ankijazz.fretboard.PngFretboardRenderer;
import com.ankijazz.fretboard.Position;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.PentatonicChooser;
import com.ankijazz.theory.Scale;
import com.ankijazz.theory.Scales;

public class PngFretboardRendererTest {

  @Test
  public void testLydianCAGEDQuestionAnswer() {
    Fretboard fretboard = new Fretboard();
    Scale fLydian = fLydian();
    Position modePosition = Marker.box(fretboard, HIGH_E_STRING, fLydian.getRoot(), RIGHT, NPS.C_MAJOR_CAGED);
    BufferedImage question = new PngFretboardRenderer(fretboard, true).render();
    //Preview.preview(question);
    TestUtils.assertImageMatches(question, getClass(), "LydianCAGEDQuestion.png");
    
    fretboard.mark(modePosition, Marker.outline(fLydian.getChord(0)));
    BufferedImage answer = new PngFretboardRenderer(fretboard, false).render();
    //Preview.preview(answer);
    TestUtils.assertImageMatches(answer, getClass(), "LydianCAGEDAnswer.png");
  }

  private Scale fLydian() {
    return Scales.CMajor.superimpose(Note.F);
  }

  @Test
  public void testLydianPentatonicQuestionAnswer() {
    Fretboard fretboard = new Fretboard();
    Scale fLydian = fLydian();
    Marker.box(fretboard, HIGH_E_STRING, fLydian.getRoot(), RIGHT, NPS.C_MAJOR_CAGED);
    BufferedImage question = new PngFretboardRenderer(fretboard, true).render();
    //Preview.preview(question);
    TestUtils.assertImageMatches(question, getClass(), "LydianPentatonicQuestion.png");
    
    PentatonicChooser chooser = new PentatonicChooser();
    Scale pentatonic = chooser.chooseBest(fLydian.getChord(0));
    fretboard.markVisible(fLydian.getRoot(), Marker.EMPTY);
    fretboard.markVisible(pentatonic, Marker.FOREGROUND);
    BufferedImage answer = new PngFretboardRenderer(fretboard, true).render();
    //Preview.preview(answer);
    TestUtils.assertImageMatches(answer, getClass(), "LydianPentatonicAnswer.png");
  }
 
}
