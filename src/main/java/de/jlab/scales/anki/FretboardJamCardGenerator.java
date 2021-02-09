package de.jlab.scales.anki;

import static de.jlab.scales.midi.song.Ensembles.funk;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.*;
import static de.jlab.scales.theory.Scales.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Ensemble;
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
  private final Iterator<SongWrapper> songFactory;
  
  final int minBpm = 60;
  final int maxBpm = 130;
  final int numberOfSongs = 20;
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
          .song(createSong(pair.getChord()))
          .build();
      return wrapper;
    }
    
    private Song createSong(Scale chord) {
      Iterator<Chord> chords = iteratorFactory.iterator(IntStream.range(0, chordsPerSong)
        .mapToObj(i -> chord.transpose(roots.next()))
        .map(scale -> new Chord(scale, ScaleUniverse.CHORDS.findFirstOrElseThrow(scale).getScaleName()))
        .collect(toList()));
      List<Bar> bars = IntStream.range(0, context.getNumberOfBars())
        .mapToObj(i -> chords.next())
        .map(c -> Bar.of(c))
        .collect(toList());
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
          
          new ScaleChordPair(CMelodicMinor, C6),
          new ScaleChordPair(CMelodicMinor.superimpose(F), C7.transpose(F)),
          new ScaleChordPair(CMelodicMinor.superimpose(B), C7sharp5.transpose(B)),
          
          new ScaleChordPair(CHarmonicMinor, CminTriad),
          new ScaleChordPair(CHarmonicMinor.superimpose(G), C7flat9)
      );
    }

  }

  Supplier<Ensemble> getEnsemble(int bpm) {
    return bpm < 90 ? () -> funk(bpm) : () -> latin(bpm);
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
