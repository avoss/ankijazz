package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.funk;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.MelodyInstrument.MELODY_MIDI_CHANNEL;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.NoteToMidiMapper;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongWrapper;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class FretboardJamCardGenerator implements CardGenerator<JamCard> {
  private final String title;
  private final String fileName;
  private final LoopIteratorFactory iteratorFactory;
  private final RenderContext context = RenderContext.ANKI;
  final Iterator<SongWrapper> songFactory;
  
  final int minBpm = 100;
  final int maxBpm = 130;
  final int numberOfSongs = 5;
  final int chordsPerSong = 4;
  

  public FretboardJamCardGenerator(String title, String fileName, LoopIteratorFactory iteratorFactory) {
    this.title = title;
    this.fileName = fileName;
    this.iteratorFactory = iteratorFactory;
    this.songFactory = new CagedModes();
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  abstract class AbstractSongWrapperFactory implements Iterator<SongWrapper> {
    
    @lombok.Data
    class ScaleChordPair {
      private final Scale scale;
      private final Scale chord;
    }

    Iterator<Note> roots = iteratorFactory.iterator(asList(Note.values()));
    Iterator<ScaleChordPair> pairs = iteratorFactory.iterator(getPairs());
    
    @Override
    public SongWrapper next() {
      ScaleChordPair pair = pairs.next();
      ScaleInfo scaleInfo = ScaleUniverse.MODES.findFirstOrElseThrow(pair.getScale());
      SongWrapper wrapper = SongWrapper.builder()
          .key("Mixed Keys")
          .progression(scaleInfo.getTypeName())
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
      Scale scale = pair.getScale();
      Iterator<Chord> chords = iteratorFactory.iterator(IntStream.range(0, chordsPerSong)
        .mapToObj(i -> pair.getChord().transpose(roots.next()))
        .map(c -> new Chord(c, ScaleUniverse.CHORDS.findFirstOrElseThrow(c).getScaleName()))
        .collect(toList()));
      List<Bar> bars = IntStream.range(0, context.getNumberOfBars()/2)
        .mapToObj(i -> chords.next())
        .flatMap(c -> {
          Scale transposed = scale.transpose(c.getScale().getRoot());
          melody.start(transposed);
          return Stream.of(Bar.of(melody.next(), c), Bar.of(melody.next(), c)); 
         }).collect(toList());
      return new Song(bars);
    }

    @Override
    public boolean hasNext() {
      return true;
    }
    
    abstract Collection<? extends ScaleChordPair> getPairs();
  }
  
  class CagedModes extends AbstractSongWrapperFactory {
    
    @Override
    Collection<? extends ScaleChordPair> getPairs() {
      return List.of(
          new ScaleChordPair(CMajor, Scales.Cmaj7),
          new ScaleChordPair(CMajor.superimpose(D), Cm7.transpose(D)),
          new ScaleChordPair(CMajor.superimpose(E), Cm7.transpose(E)),
          new ScaleChordPair(CMajor.superimpose(F), Cmaj7.transpose(F)),
          new ScaleChordPair(CMajor.superimpose(G), C7.transpose(G)),
          new ScaleChordPair(CMajor.superimpose(A), Cm7.transpose(A)),
          new ScaleChordPair(CMajor.superimpose(B), Cm7b5.transpose(B)),
          
          new ScaleChordPair(CMelodicMinor, Cm6),
          new ScaleChordPair(CMelodicMinor.superimpose(F), C7.transpose(F)),
          new ScaleChordPair(CMelodicMinor.superimpose(B), C7sharp5.transpose(B)),
          
          new ScaleChordPair(CHarmonicMinor, CminTriad),
          new ScaleChordPair(CHarmonicMinor.superimpose(G), C7flat9)
      );
    }

  }

  Supplier<Ensemble> getEnsemble(int bpm) {
    return () -> latin(bpm);
  }
  
  @Override
  public Collection<? extends JamCard> generate() {
    List<JamCard> cards = new ArrayList<>();
    Iterator<FretboardPosition> positions = iteratorFactory.iterator(asList(FretboardPosition.values()));
    Interpolator bpmInterpolator = Utils.interpolator(0, numberOfSongs, minBpm, maxBpm);
    for (int songNumber = 0; songNumber < numberOfSongs; songNumber ++) {
      SongWrapper wrapper = songFactory.next();
      FretboardPosition position = positions.next();
      int bpm = bpmInterpolator.apply(songNumber);
      Supplier<Ensemble> ensemble = getEnsemble(bpm);
      cards.add(new JamCard(Note.C, context, wrapper, ensemble, position));
    }
    return cards;
  }

}
