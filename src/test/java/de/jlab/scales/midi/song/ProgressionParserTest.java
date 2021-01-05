package de.jlab.scales.midi.song;

import static de.jlab.scales.TestUtils.majorKeySignature;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.BOF;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.CHORD;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.END_BAR;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.END_CHOICE;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.EOF;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.START_BAR;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.START_CHOICE;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.Utils;
import de.jlab.scales.midi.song.ProgressionParser.ChordFactory;
import de.jlab.scales.midi.song.ProgressionParser.Scanner;
import de.jlab.scales.midi.song.ProgressionParser.SimpleChordFactory;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scales;

public class ProgressionParserTest {

  @Test
  public void testScanner() {
    Scanner scanner = new Scanner("[(Ab7#5)] CmΔ7 Dm7");
    assertEquals(BOF, scanner.getToken());
    assertEquals(START_BAR, scanner.next());
    assertEquals(START_CHOICE, scanner.next());
    assertEquals(CHORD, scanner.next());
    assertEquals("Ab7#5", scanner.getValue());
    assertEquals(END_CHOICE, scanner.next());
    assertEquals(END_BAR, scanner.next());
    assertEquals(CHORD, scanner.next());
    assertEquals("CmΔ7", scanner.getValue());
    assertEquals(CHORD, scanner.next());
    assertEquals("Dm7", scanner.getValue());
    assertEquals(EOF, scanner.next());
    assertEquals(EOF, scanner.next());
  }

  @Test
  public void testSimpleChordFactory() {
    ChordFactory factory = new SimpleChordFactory("CΔ7");
    KeySignature keyOfG = majorKeySignature(Note.G);
    List<Chord> list = factory.create(keyOfG);
    assertEquals(1, list.size());
    assertEquals(Chord.of(Scales.Cmaj7.transpose(Note.G), "GΔ7"), list.get(0));
  }
  
  @Test
  public void testProgressionParser() {
    assertProgression("Dm7 Em7", "| Em7 | F#m7 |");
    assertProgression("[Dm7 Em7]", "| Em7 F#m7 |");
    assertProgression("(Dm7 Em7)", "| Em7 |", "| F#m7 |");
    assertProgression("(Dm7 (Em7 Fm7))", "| Em7 |", "| F#m7 |", "| Em7 |", "| Gm7 |");
    assertProgression("(Dm7 [Em7 Fm7])", "| Em7 |", "| F#m7 Gm7 |");
    assertProgression("Am7 [Dm7 (Em7 Fm7)]", "| Bm7 | Em7 F#m7 |", "| Bm7 | Em7 Gm7 |");
  }

  private void assertProgression(String progression, String ... songs) {
    ProgressionParser parser = new ProgressionParser(Utils.fixedLoopIteratorFactory());
    List<ChordFactory> factories = parser.parse(progression);
    KeySignature keyOfD = majorKeySignature(Note.D);
    for (String expected : songs) {
      List<Bar> bars = factories.stream().map(cf -> cf.create(keyOfD)).map(chords -> new Bar(chords)).collect(toList());
      Song song = new Song(bars);
      assertEquals(expected, song.toString());
    }
  }
}
