package de.jlab.scales.theory;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.jlab.scales.theory.DegreeParser.Degree;

public class DegreeParserTest {
  DegreeParser parser = new DegreeParser();
  
  private Degree d(int index, int offset) {
    return new Degree(index, offset);
  }

  @Test
  public void testParse() {
    assertThat(parser.parse("1 b3 5 b7")).containsExactly(d(0, 0), d(2, -1), d(4, 0), d(6, -1));
    assertThat(parser.parse("1 bb3 #4 x7")).containsExactly(d(0, 0), d(2, -2), d(3, 1), d(6, 2));
    assertThat(parser.parse("xb1")).containsExactly(d(0,1));
  }
  
  @Test
  public void playground() {
    print(BuiltinChordType.AugmentedTriad, "1 3 #5");
    print(BuiltinChordType.Minor7b5, "1 b3 b5 b7");
    print(BuiltinChordType.Diminished7, "1 b3 b5 bb7");
  }
  
  private void print(BuiltinChordType type, String string) {
    System.out.println("\nType " + type.getTypeName() + ":");
    List<Degree> degrees = parser.parse(string);
    for (Note chordRoot : Note.values()) {
      Scale scale = Scales.CMajor.transpose(chordRoot);//.superimpose(chordRoot);
      System.out.print(spellChord(scale, degrees));
    }
  }

  private String spellChord(Scale scale, List<Degree> degrees) {
    StringBuilder sb = new StringBuilder();
    for (KeySignature keySignature : BuiltinScaleType.Major.getKeySignatures(scale.getRoot())) {
      Map<Note, Accidental> accidentalMap = keySignature.getAccidentalMap();
      for (Degree degree : degrees) {
        Note scaleNote = scale.getNote(degree.getNoteIndexInScale());
        Accidental appliedAccidental = accidentalMap.get(scaleNote);
        Note cmajorNote = appliedAccidental.inverse().apply(scaleNote);
        Accidental newAccidental = Accidental.fromOffset(appliedAccidental.offset() + degree.getOffsetToApply());
        sb.append(cmajorNote.name() + newAccidental.symbol() + " ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
