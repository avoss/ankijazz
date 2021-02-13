package de.jlab.scales.anki;

import static de.jlab.scales.theory.BuiltinChordType.Dominant7;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sharp5;
import static de.jlab.scales.theory.BuiltinChordType.Dominant7sus4;
import static de.jlab.scales.theory.BuiltinChordType.Major6;
import static de.jlab.scales.theory.BuiltinChordType.Major7;
import static de.jlab.scales.theory.BuiltinChordType.Major7Sharp11;
import static de.jlab.scales.theory.BuiltinChordType.Minor6;
import static de.jlab.scales.theory.BuiltinChordType.Minor7;
import static de.jlab.scales.theory.BuiltinChordType.Minor7b5;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.jlab.scales.anki.FretboardJamCardGenerator.ScaleChordPair;
import de.jlab.scales.theory.BuiltinChordType;
import de.jlab.scales.theory.BuiltinScaleType;
import de.jlab.scales.theory.PentatonicChooser;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleType;
import de.jlab.scales.theory.Scales;

@lombok.Data
@lombok.Builder
public class ChordScaleAudio {
  private final Scale chord;
  private final Scale scale;
  private final Scale audio;
  private final String title;
  private final String comment;

  
  public static List<ChordScaleAudio> cagedScales() {
    List<ChordScaleAudio> result = new ArrayList<>();
    for (Scale scale : Scales.commonScales(false)) {
      Scale chord = scale.getChord(0);
      result.add(builder()
          .chord(chord)
          .scale(scale)
          .audio(chord)
          .build());
    }
    return result;
  }

  public static List<ChordScaleAudio> cagedModes() {
    List<ChordScaleAudio> result = new ArrayList<>();
    for (Scale scale : Scales.commonModes(false)) {
      Scale chord = substituteAlteredChord(scale);
      result.add(builder()
          .chord(chord)
          .scale(scale)
          .audio(chord)
          .build());
    }
    return result;
  }
  
  private static Scale substituteAlteredChord(Scale scale) {
    if (scale.isAlteredDominant()) {
      return Scales.C7sharp5.transpose(scale.getRoot());
    }
    return scale.getChord(0);
  }

  public static List<ChordScaleAudio> pentatonicScales() {
    return List.of(
        builder()
          .chord(Cm7)
          .scale(CMinor7Pentatonic)
          .audio(Cm7)
          .build(),
        builder()
          .chord(Cm6)
          .scale(CMinor6Pentatonic)
          .audio(Cm6)
          .build());
  }

  public static List<ChordScaleAudio> pentatonicChords() {
    List<ChordScaleAudio> result = new ArrayList<>();
    
    result.add(builder()
      .chord(Cm7)
      .scale(CMinor7Pentatonic)
      .audio(Cm7)
      .title("Outline Minor7 Chord")
      .comment("Play Minor7 Pentatonic at root of Minor7 Chord")
      .build());
    
    result.add(builder()
      .chord(Cmaj7)
      .scale(CMinor7Pentatonic.transpose(E))
      .audio(Cmaj7)
      .title("Outline Major7 Chord")
      .comment("Play Minor7 Pentatonic at major 3rd of Major7 Chord")
      .build());
    
    result.add(builder()
      .chord(Cmaj7Sharp11)
      .scale(CMinor7Pentatonic.transpose(B))
      .audio(Cmaj7)
      .title("Outline Major7#11 Chord")
      .comment("Play Minor7 Pentatonic at major 7th of Major7#11 Chord")
      .build());
    
    result.add(builder()
      .chord(C6)
      .scale(CMinor7Pentatonic.transpose(A))
      .audio(C6)
      .title("Outline Major6 Chord")
      .comment("Play Minor7 Pentatonic at major 6th of Major6 Chord")
      .build());
    
    result.add(builder()
      .chord(C7sus4)
      .scale(CMinor7Pentatonic.transpose(G))
      .audio(C7sus4)
      .title("Outline 7Sus4 Chord")
      .comment("Play Minor7 Pentatonics at 5th of 7Sus4 Chord")
      .build());
    
    result.add(builder()
      .chord(Cm6)
      .scale(CMinor6Pentatonic)
      .audio(Cm6)
      .title("Outline Minor6 Chord")
      .comment("Play Minor6 Pentatonics at root of Minor6 Chord")
      .build());
    
    result.add(builder()
      .chord(C7)
      .scale(CMinor6Pentatonic.transpose(G))
      .audio(C7)
      .title("Outline Dominant 7th Chord")
      .comment("Play Minor6 Pentatonics at 5th of Dominant 7th Chord")
      .build());
    
    result.add(builder()
      .chord(C7sharp5)
      .scale(CMinor6Pentatonic.transpose(Db))
      .audio(C7sharp5)
      .title("Outline Altered Dominant Chord")
      .comment("Play Minor6 Pentatonics at b9 of altered Chord")
      .build());
    
    result.add(builder()
      .chord(Cm7b5)
      .scale(CMinor6Pentatonic.transpose(Eb))
      .audio(Cm7b5)
      .title("Outline Minor7b5 Chord")
      .comment("Play Minor6 Pentatonics at minor 3rd of Minor7b5 Chord")
      .build());
    
    return result;
  }

  public static List<ChordScaleAudio> pentatonicModes() {
    List<ChordScaleAudio> result = new ArrayList<>();
    PentatonicChooser chooser = new PentatonicChooser();
    for (Scale scale : Scales.commonModes(false)) {
      if (!scaleContainsPentatonic(scale)) {
        continue;
      }
      Scale chord = scale.getChord(0);
      Scale penta = chooser.chooseBest(chord);
      result.add(builder()
          .chord(penta)
          .scale(scale)
          .audio(chord)
          .build());
    }
    return result;
  }

  static boolean scaleContainsPentatonic(Scale scale) {
    ScaleType type = MODES.findFirstOrElseThrow(scale).getScaleType();
    return Set.of(BuiltinScaleType.Major, BuiltinScaleType.MelodicMinor).contains(type);
  }
}