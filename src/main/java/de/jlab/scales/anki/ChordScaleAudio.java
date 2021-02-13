package de.jlab.scales.anki;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.C6;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C7sus4;
import static de.jlab.scales.theory.Scales.CHarmonicMinor;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.CMelodicMinor;
import static de.jlab.scales.theory.Scales.CMinor6Pentatonic;
import static de.jlab.scales.theory.Scales.CMinor7Pentatonic;
import static de.jlab.scales.theory.Scales.Cm6;
import static de.jlab.scales.theory.Scales.Cm7;
import static de.jlab.scales.theory.Scales.Cm7b5;
import static de.jlab.scales.theory.Scales.Cmaj7;
import static de.jlab.scales.theory.Scales.Cmaj7Sharp11;
import static de.jlab.scales.theory.Scales.Cmmaj7;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.jlab.scales.theory.BuiltinScaleType;
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
    for (Scale scale : CMajor.getInversions()) {
      Scale chord = scale.getChord(0);
      result.add(builder()
          .chord(chord)
          .scale(scale)
          .audio(chord)
          .build());
    }
    result.add(builder()
      .chord(Cmmaj7)
      .scale(CMelodicMinor)
      .audio(Cmmaj7)
      .build());

    result.add(builder()
        .chord(C7.transpose(F))
        .scale(CMelodicMinor.superimpose(F))
        .audio(C7.transpose(F))
        .build());

    result.add(builder()
        .chord(C7sharp5.transpose(B))
        .scale(CMelodicMinor.superimpose(B))
        .audio(C7sharp5.transpose(B))
        .build());

    result.add(builder()
        .chord(Cmmaj7)
        .scale(CHarmonicMinor)
        .audio(Cmmaj7)
        .build());

    result.add(builder()
        .chord(C7flat9.transpose(G))
        .scale(CHarmonicMinor.superimpose(G))
        .audio(C7flat9.transpose(G))
        .build());
    
    return result;
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
    
    result.add(builder()
      .chord(CMinor7Pentatonic.transpose(E))
      .scale(CMajor)
      .audio(Cmaj7)
      .title("Outline Ionean Mode")
      .comment("Play Minor7 Pentatonic at 3rd degree of mode")
      .build());
    
    result.add(builder()
      .chord(CMinor7Pentatonic.transpose(D))
      .scale(CMajor.superimpose(D))
      .audio(Cm7.transpose(D))
      .title("Outline Dorian Mode")
      .comment("Play Minor7 Pentatonic at 1st degree of mode")
      .build());

    result.add(builder()
        .chord(CMinor7Pentatonic.transpose(E))
        .scale(CMajor.superimpose(E))
        .audio(Cm7.transpose(E))
        .title("Outline Prhygian Mode")
        .comment("Play Minor7 Pentatonic at 1st degree of mode")
        .build());
    
    result.add(builder()
        .chord(CMinor7Pentatonic.transpose(E))
        .scale(CMajor.superimpose(F))
        .audio(Cmaj7.transpose(F))
        .title("Outline Lydian Mode")
        .comment("Play Minor7 Pentatonic at 7th degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor6Pentatonic.transpose(D))
        .scale(CMajor.superimpose(G))
        .audio(C7.transpose(G))
        .title("Outline Mixolydian Mode")
        .comment("Play Minor6 Pentatonic at 5th degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor7Pentatonic.transpose(A))
        .scale(CMajor.superimpose(A))
        .audio(Cm7.transpose(A))
        .title("Outline Aeolean Mode")
        .comment("Play Minor7 Pentatonic at 1st degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor6Pentatonic.transpose(D))
        .scale(CMajor.superimpose(B))
        .audio(Cm7b5.transpose(B))
        .title("Outline Locrian Mode")
        .comment("Play Minor6 Pentatonic at 3rd degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor6Pentatonic)
        .scale(CMelodicMinor.superimpose(C))
        .audio(Cmmaj7.transpose(C))
        .title("Outline Melodic Minor")
        .comment("Play Minor6 Pentatonic at 1st degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor6Pentatonic)
        .scale(CMelodicMinor.superimpose(F))
        .audio(C7.transpose(F))
        .title("Outline Lydian Dominant")
        .comment("Play Minor6 Pentatonic at 5th degree of mode")
        .build());

    result.add(builder()
        .chord(CMinor6Pentatonic)
        .scale(CMelodicMinor.superimpose(B))
        .audio(C7sharp5.transpose(B))
        .title("Outline Lydian Dominant")
        .comment("Play Minor6 Pentatonic at b9 degree of mode")
        .build());
    
    return result;
  }

  static boolean scaleContainsPentatonic(Scale scale) {
    ScaleType type = MODES.findFirstOrElseThrow(scale).getScaleType();
    return Set.of(BuiltinScaleType.Major, BuiltinScaleType.MelodicMinor).contains(type);
  }
}