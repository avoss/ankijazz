package de.jlab.scales.theory;

import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.assertj.core.util.Arrays;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import de.jlab.scales.theory.Scales.ScaleInfos.Formatter.FormatterBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

public class Scales {

  public static final Scale CMajor = new Scale(C, D, E, F, G, A, B);
  public static final Scale CMelodicMinor = new Scale(C, D, Eb, F, G, A, B);
  public static final Scale CHarmonicMinor = new Scale(C, D, Eb, F, G, Ab, B);
  public static final Scale CHarmonicMajor = new Scale(C, D, E, F, G, Ab, B);
  public static final Scale CDiminishedHalfWhole = new Scale(C, Db, Eb, E, Gb, G, A, Bb);
  public static final Scale CWholeTone = new Scale(C, D, E, Gb, Ab, Bb);

  public static final Scale CMinorPentatonic = new Scale(C, Eb, F, G, Bb);
  public static final Scale CMinor6Pentatonic = new Scale(C, Eb, F, G, A);

  public static final Scale Cmaj7 = new Scale(C, E, G, B);
  public static final Scale Cm7 = new Scale(C, Eb, G, Bb);
  public static final Scale Cm6 = new Scale(C, Eb, G, A);
  public static final Scale C7 = new Scale(C, E, G, Bb);
  public static final Scale Cm7b5 = new Scale(C, Eb, Gb, Bb);
  public static final Scale Cdim7 = new Scale(C, Eb, Gb, A);
  public static final Scale Cmmaj7 = new Scale(C, Eb, G, B);
  public static final Scale Cmaj7sharp5 = new Scale(C, E, Ab, B);
  public static final Scale C7sus4 = new Scale(C, F, G, Bb);

  public static final Scale C7flat9 = new Scale(C, E, G, Bb, Db);
  public static final Scale C7sharp9 = new Scale(C, E, G, Bb, Eb);
  public static final Scale C7flat5 = new Scale(C, E, Gb, Bb);
  public static final Scale C7sharp5 = new Scale(C, E, Ab, Bb);
  public static final Scale C7flat5flat9 = new Scale(C, E, Gb, Bb, Db);
  public static final Scale C7sharp5flat9 = new Scale(C, E, Ab, Bb, Db);
  public static final Scale C7flat5sharp9 = new Scale(C, E, Gb, Bb, Eb);
  public static final Scale C7sharp5sharp9 = new Scale(C, E, Ab, Bb, Eb);

  public static final Scale CmajTriad = new Scale(C, E, G);
  public static final Scale CminTriad = new Scale(C, Eb, G);
  public static final Scale CdimTriad = new Scale(C, Eb, Gb);
  public static final Scale CaugTriad = new Scale(C, E, Ab);
  public static final Scale CsusTriad = new Scale(C, F, G);

  static class ScaleInfos {

    private ScaleInfos() {
      scale(CMajor, "Major Scale", "Ionian", "Dorian", "Phrygian", "Lydian", "Mixolydian", "Aeolean", "Locrian");
      scale(CMelodicMinor, "Melodic Minor", "Melodic Minor", "Phrygian #6", "Lydian #5", "Lydian Dominant", "Aeolean #3", "Altered", "Ionian #1");
      scale(CHarmonicMinor, "Harmonic Minor", "Harmonic Minor", "Locrian #6", "Ionian #5", "Dorian #4", "Phrygian Dominant", "Lydian #2", "Mixolydian #1");
      scale(CHarmonicMajor, "Harmonic Major", "Ionian b6", "Dorian b5", "Phrygian b4", "Lydian b3", "Mixolydian b2", "Aeolean b1", "Locrian b7");
      scale(CDiminishedHalfWhole, "Diminished Half/Whole");
      scale(CWholeTone, "Whole Tone");

      scale(CMinorPentatonic, "Minor Pentatonic");
      scale(CMinor6Pentatonic, "Minor6 Pentatonic");

      chord(Cmaj7, "Δ7");
      chord(Cm7, "-7");
      chord(Cm6, "-6");
      chord(C7, "7");
      chord(Cm7b5, "ø");
      chord(Cdim7, "o7");
      chord(Cmmaj7, "mΔ7");
      chord(Cmaj7sharp5, "Δ7#5");
      chord(C7sus4, "7sus");
      chord(C7flat9, "7b9");
      chord(C7sharp9, "7#9");
      chord(C7flat5, "7b5");
      chord(C7sharp5, "7#5");
      chord(C7flat5flat9, "7b5b9");
      chord(C7sharp5flat9, "7#5b9");
      chord(C7flat5sharp9, "7b5#9");
      chord(C7sharp5sharp9, "7#5#9");

      chord(CmajTriad, "");
      chord(CminTriad, "m");
      chord(CdimTriad, "o");
      chord(CaugTriad, "+");
      chord(CsusTriad, "sus4");
    }

    private ListMultimap<Scale, ScaleInfo> infos = MultimapBuilder.hashKeys().arrayListValues().build();
    
    @Builder
    static class Formatter {
      // {0} mode root name
      // {1} mode||scale name
      // {2} parent root name
      private final String namePattern;
      private final String fallbackPattern;
      private final String scaleName;
      private final String[] modeNames;
      private final Accidental accidental;
      
      public String name(int index, Note oldRoot, Note newRoot) {
        String oldRootName = oldRoot.getName(accidental);
        String newRootName = newRoot.getName(accidental);
        String modeName = modeName(index);
        if (modeName != null) {
          return MessageFormat.format(namePattern, newRootName, modeName, oldRootName);
        }
        return MessageFormat.format(fallbackPattern, newRootName, scaleName, oldRootName);
      }

      private String modeName(int index) {
        if (index == 0) {
          return scaleName;
        }
        if (index < modeNames.length) {
          return modeNames[index];
        }
        return null;
      }
    }
    
    private void scale(Scale scale, String scaleName, String... modeNames) {
      FormatterBuilder fmt = Formatter.builder()
        .namePattern("{0} {1}")
        .scaleName(scaleName)
        .fallbackPattern("{2} {1}/{0}")
        .modeNames(modeNames);
      addAll(scale, fmt);
    }
    
    private void chord(Scale scale, String scaleName, String... modeNames) {
      FormatterBuilder fmt = Formatter.builder()
          .namePattern("{0}{1}")
          .scaleName(scaleName)
          .fallbackPattern("{2}{1}/{0}")
          .modeNames(modeNames);
        addAll(scale, fmt);
    }

    private void addAll(Scale scale, FormatterBuilder formatter) {
      for (Note newRoot : Note.values()) {
        Scale transposed = scale.transpose(newRoot);
        addModes(transposed, formatter.accidental(newRoot.getAccidental()).build());
      }
    }

    private void addModes(Scale parent, Formatter formatter) {
      Note oldRoot = parent.getRoot();
      Accidental accidental = oldRoot.getAccidental();
      for (int i = 0; i < parent.length(); i++) {
        Note newRoot = parent.getNote(i);
        Scale mode = parent.superimpose(newRoot);
        ScaleInfo info = ScaleInfo.builder()
          .accidental(accidental)
          .scale(mode)
          .parent(parent)
          .name(formatter.name(i, oldRoot, newRoot))
          .build();
        infos.put(mode, info);
      }
    }

    private final Comparator<ScaleInfo> infoQuality = (a, b) -> { 
        if (a.isInversion() == b.isInversion()) {
          return 0;
        }
        return a.isInversion() ? 1 : -1;
    };
    
    public List<ScaleInfo> get(Scale scale) {
      if (infos.containsKey(scale)) {
        return infos.get(scale).stream().sorted(infoQuality).collect(toList());
      }
      ScaleInfo info = defaultInfo(scale);
      infos.put(scale, info);
      return Collections.singletonList(info);
    }

    private ScaleInfo defaultInfo(Scale scale) {
      Accidental accidental = scale.getRoot().getAccidental();
      return ScaleInfo.builder()
        .accidental(accidental)
        .scale(scale)
        .name(scale.asChord(accidental))
        .parent(scale).build();
    }
  }

  public static ScaleInfo info(Scale scale) {
    return infos.get(scale).stream().findFirst().get();
  }
  
  public static Collection<ScaleInfo> infos(Scale scale) {
    return infos.get(scale);
  }

  private static final ScaleInfos infos = new ScaleInfos();
  
}
