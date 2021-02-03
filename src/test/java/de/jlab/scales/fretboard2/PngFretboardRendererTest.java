package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.BoxMarker.BoxPosition.RIGHT;

import java.awt.image.BufferedImage;
import java.util.Optional;

import org.junit.Test;

import de.jlab.scales.TestUtils;
import de.jlab.scales.theory.ChordSubstitutionChooser.SubstitutionInfo;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.PentatonicChooser;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.Scales;

public class PngFretboardRendererTest {

  @Test
  public void testLydianCAGEDQuestionAnswer() {
    Fretboard fretboard = new Fretboard();
    Scale fLydian = fLydian();
    Position modePosition = Markers.box(fretboard, 5, fLydian.getRoot(), RIGHT, NPS.C_MAJOR_CAGED);
    BufferedImage question = new PngFretboardRenderer(fretboard).render();
    //Preview.preview(question);
    TestUtils.assertImageMatches(question, getClass(), "LydianCAGEDQuestion.png");
    
    fretboard.mark(modePosition, Markers.marker(fLydian.getChord(0)));
    BufferedImage answer = new PngFretboardRenderer(fretboard).render();
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
    Markers.box(fretboard, 5, fLydian.getRoot(), RIGHT, NPS.C_MAJOR_CAGED);
    BufferedImage question = new PngFretboardRenderer(fretboard).render();
    //Preview.preview(question);
    TestUtils.assertImageMatches(question, getClass(), "LydianPentatonicQuestion.png");
    
    PentatonicChooser chooser = new PentatonicChooser();
    Scale pentatonic = chooser.chooseBest(fLydian.getChord(0));
    fretboard.markVisible(fLydian.getRoot(), Markers.empty());
    fretboard.markVisible(pentatonic, Markers.foreground());
    BufferedImage answer = new PngFretboardRenderer(fretboard).render();
    //Preview.preview(answer);
    TestUtils.assertImageMatches(answer, getClass(), "LydianPentatonicAnswer.png");
    
  }
  
}
