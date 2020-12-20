package de.jlab.scales.lily;

import static de.jlab.scales.theory.BuiltinChordType.Diminished7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.Db;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.lily.ChordToLilyMapper.LilyChord;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleType;

public class ChordToLilyMapperTest {

  @Test
  public void testChordToLilyMapper() {
    assertEquals(new LilyChord("a", "b"), new LilyChord("a", "b"));
    assertLilyChord(Major7, C, "c1:maj7", "<c e g b>1");
    assertLilyChord(Dominant7, Bb, "bf1:7", "<bf d f af>1");
    assertLilyChord(Diminished7, Db, "df1:dim7", "<df e g bf>1");
  }
  
  private void assertLilyChord(ScaleType type, Note root, String expectedChord, String expectedNotes) {
    ChordToLilyMapper mapper = new ChordToLilyMapper();
    Scale chord = type.getPrototype().transpose(root);
    KeySignature keySignature = type.getKeySignatures(root).stream().findFirst().get();
    LilyChord actual = mapper.chordToLily(keySignature, chord, 1);
    assertEquals(new LilyChord(expectedChord, expectedNotes), actual);
  }
  
  @Test
  public void testMappingIsComplete() {
    ChordToLilyMapper mapper = new ChordToLilyMapper();
    for (ScaleType type : BuiltinChordType.values()) {
      for (Note root : Note.values()) {
        for (KeySignature keySignature : type.getKeySignatures(root)) {
          Scale chord = type.getPrototype().transpose(root);
          mapper.chordToLily(keySignature, chord, 1);
        }
      }
    }
  }

}
