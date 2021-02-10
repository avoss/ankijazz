package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.MelodyInstrument.MELODY_MIDI_CHANNEL;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.C6;
import static de.jlab.scales.theory.Scales.C7;
import static de.jlab.scales.theory.Scales.C7flat9;
import static de.jlab.scales.theory.Scales.C7sharp5;
import static de.jlab.scales.theory.Scales.C7sharp5flat9;
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
import static de.jlab.scales.theory.Scales.CminTriad;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.NoteToMidiMapper;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongWrapper;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class FretboardJamCardGenerator implements CardGenerator<JamCard> {
  private final LoopIteratorFactory iteratorFactory;
  private final RenderContext context = RenderContext.ANKI;
  private final Spec spec;
  final Iterator<SongWrapper> songFactory;
  
  final int minBpm = 100;
  final int maxBpm = 130;
  final int numberOfSongs;
  final int chordsPerSong = 4;
  final int songsPerPair = 12;

  @lombok.Data
  @lombok.RequiredArgsConstructor
  @lombok.AllArgsConstructor
  static class ScaleChordPair {
    private final Scale scale;
    private final Scale chord;
    private String title;
  }
  
  @lombok.Builder
  @lombok.Data
  static class Spec {
    private final String title;
    private final String fileName;
    @lombok.Singular
    private final List<ScaleChordPair> pairs;
  }

  
  public static final Spec CAGED_MODES = Spec.builder()
      .title("CAGED Level 4: Practice Mode Positions (Jam Tracks)")
      .fileName("Caged4PracticeModes")
      .pair(new ScaleChordPair(CMajor, Scales.Cmaj7))
      .pair(new ScaleChordPair(CMajor.superimpose(D), Cm7.transpose(D)))
      .pair(new ScaleChordPair(CMajor.superimpose(E), Cm7.transpose(E)))
      .pair(new ScaleChordPair(CMajor.superimpose(F), Cmaj7.transpose(F)))
      .pair(new ScaleChordPair(CMajor.superimpose(G), C7.transpose(G)))
      .pair(new ScaleChordPair(CMajor.superimpose(A), Cm7.transpose(A)))
      .pair(new ScaleChordPair(CMajor.superimpose(B), Cm7b5.transpose(B)))
          
      .pair(new ScaleChordPair(CMelodicMinor, Cm6))
      .pair(new ScaleChordPair(CMelodicMinor.superimpose(F), C7.transpose(F)))
      .pair(new ScaleChordPair(CMelodicMinor.superimpose(B), C7sharp5.transpose(B)))
          
      .pair(new ScaleChordPair(CHarmonicMinor, CminTriad))
      .pair(new ScaleChordPair(CHarmonicMinor.superimpose(G), C7flat9.transpose(G)))
      .build(); 

  public static final Spec CAGED_SCALES = Spec.builder()
      .title("CAGED Level 2: Practice Scale Positions (Jam Tracks)")
      .fileName("Caged2PracticeScales")
      .pair(new ScaleChordPair(CMajor, Scales.Cmaj7))
      .pair(new ScaleChordPair(CMelodicMinor, Cm6))
      .pair(new ScaleChordPair(CHarmonicMinor, CminTriad))
      .build(); 

  public static final Spec PENTATONIC_SCALES = Spec.builder()
      .title("Pentatonics 2: Practice Scale Positions (Jam Tracks)")
      .fileName("Pentatonics2PracticeScales")
      .pair(new ScaleChordPair(CMinor7Pentatonic, Cm7))
      .pair(new ScaleChordPair(CMinor6Pentatonic, Cm6))
      .build(); 

  public static final Spec PENTATONIC_CHORDS = Spec.builder()
      .title("Pentatonics 4: Outline Chords(Jam Tracks)")
      .fileName("Pentatonics4OutlineChords")
      .pair(new ScaleChordPair(CMinor7Pentatonic, Cm7, "Outline Minor7 Chord"))
      .pair(new ScaleChordPair(CMinor7Pentatonic.transpose(E), Cmaj7, "Outline Major7 Chord"))
      .pair(new ScaleChordPair(CMinor7Pentatonic.transpose(B), Cmaj7, "Outline Major7#11 Chord"))
      .pair(new ScaleChordPair(CMinor7Pentatonic.transpose(A), C6, "Outline Major6 Chord"))
      .pair(new ScaleChordPair(CMinor7Pentatonic.transpose(G), C7sus4, "Outline 7Sus4 Chord"))
      
      .pair(new ScaleChordPair(CMinor6Pentatonic, Cm6, "Outline Minor6 Chord"))
      .pair(new ScaleChordPair(CMinor6Pentatonic.transpose(G), C7, "Outline Dominant 7th Chord"))
      .pair(new ScaleChordPair(CMinor6Pentatonic.transpose(Db), C7sharp5flat9, "Outline Altered Dominant Chord"))
      .pair(new ScaleChordPair(CMinor6Pentatonic.transpose(Eb), Cm7b5, "Outline Minor7b5 Chord"))
      .build(); 
  
  public FretboardJamCardGenerator(Spec spec, LoopIteratorFactory iteratorFactory) {
    this.spec = spec;
    this.iteratorFactory = iteratorFactory;
    this.numberOfSongs = spec.getPairs().size() * songsPerPair;
    this.songFactory = new SongWrapperFactory();
  }

  @Override
  public String getTitle() {
    return spec.getTitle();
  }

  @Override
  public String getFileName() {
    return spec.getFileName();
  }

  class SongWrapperFactory implements Iterator<SongWrapper> {
    
    Iterator<Note> roots = iteratorFactory.iterator(asList(Note.values()));
    Iterator<ScaleChordPair> pairs = iteratorFactory.iterator(spec.getPairs());
    
    @Override
    public SongWrapper next() {
      ScaleChordPair pair = pairs.next();
      ScaleInfo scaleInfo = ScaleUniverse.MODES.findFirstOrElseThrow(pair.getScale());
      SongWrapper wrapper = SongWrapper.builder()
          .key("Mixed Keys")
          .progression(pair.getTitle() == null ? scaleInfo.getTypeName() : pair.getTitle())
          .progressionSet(scaleInfo.getScaleType().getTypeName())
          .song(createSong(pair))
          .build();
      return wrapper;
    }

    class Melody {
      private final int minPitch = 56;
      private NoteToMidiMapper mapper = NoteToMidiMapper.range(minPitch, minPitch + 24);
      boolean ascending;
      private Iterator<Note> iterator;
      private int melodyVelocity = 127;
      
      void start(Scale scale) {
        ascending = !ascending;
        List<Note> list = scale.asList();
        list.add(list.get(0));
        if (!ascending) {
          Collections.reverse(list);
        }
        iterator = list.iterator();
        if (ascending) {
          mapper.resetToLowest();
        } else {
          mapper.resetToHighest();
        }
      }
      
      Part next() {
        Sequential seq = Parts.seq();
        for (int i = 4; i > 0; i--) {
          if (iterator.hasNext()) {
            Note note = iterator.next();
            int pitch = mapper.nextClosest(note);
            seq.add(Parts.note(MELODY_MIDI_CHANNEL, pitch, melodyVelocity , 4));
            seq.add(Parts.rest(4));
          }
        }
        return seq;
      }
    }
    
    Melody melody = new Melody();
    
    private Song createSong(ScaleChordPair pair) {
      Iterator<Integer> semitonesIterator = iteratorFactory.iterator(IntStream.range(0, chordsPerSong).map(i -> roots.next().ordinal()).boxed().collect(toList()));
      List<Bar> bars = new ArrayList<>();
      for (int i = 0; i < context.getNumberOfBars() / 2; i++) {
        int semitones = semitonesIterator.next();
        Scale transposedChord = pair.getChord().transpose(semitones);
        Chord chord = new Chord(transposedChord, ScaleUniverse.CHORDS.findFirstOrElseThrow(transposedChord).getScaleName());
        Scale transposedScale = pair.getScale().transpose(semitones);
        melody.start(transposedScale);
        bars.add(Bar.of(melody.next(), chord));
        bars.add(Bar.of(melody.next(), chord));
      }
      return new Song(bars);
    }

    @Override
    public boolean hasNext() {
      return true;
    }
  }

  @Override
  public Collection<? extends JamCard> generate() {
    List<JamCard> cards = new ArrayList<>();
    Iterator<FretboardPosition> positions = iteratorFactory.iterator(asList(FretboardPosition.values()));
    for (int songNumber = 0; songNumber < numberOfSongs; songNumber ++) {
      SongWrapper wrapper = songFactory.next();
      FretboardPosition position = positions.next();
      cards.add(new JamCard(Note.C, context, wrapper, () -> latin(120), position));
    }
    return cards;
  }

}
