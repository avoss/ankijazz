package de.jlab.scales.anki;

import static de.jlab.scales.Utils.getFirst;
import static de.jlab.scales.Utils.getLast;
import static de.jlab.scales.midi.song.Ensembles.latin;
import static de.jlab.scales.midi.song.MelodyInstrument.MELODY_MIDI_CHANNEL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;
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

public class FretboardJamCardGenerator implements CardGenerator<JamCard> {
  private final LoopIteratorFactory iteratorFactory;
  private final RenderContext context = RenderContext.ANKI;
  private final Spec spec;
  private final int numberOfSongs;
  private final int songsPerChordScalePair = 36;
  final int numberOfBarsPerChord = 2;
  final Iterator<SongWrapper> songFactory;
 
  @lombok.Builder
  @lombok.Data
  static class Spec {
    private final String title;
    private final String fileName;
    private final List<ChordScaleAudio> pairs;
  }
  
  public static final Spec CAGED_MODES = Spec.builder()
      .title("AnkiJazz Guitar - CAGED 4: Modes (Jamtracks)")
      .fileName("Caged4ModesJamtracks")
      .pairs(ChordScaleAudio.cagedModes())
      .build(); 

  public static final Spec CAGED_SCALES = Spec.builder()
      .title("AnkiJazz Guitar - CAGED 2: Scales (Jamtracks)")
      .fileName("Caged2ScalesJamtracks")
      .pairs(ChordScaleAudio.cagedScales())
      .build(); 

  public static final Spec PENTATONIC_SCALES = Spec.builder()
      .title("AnkiJazz Guitar - Pentatonics 2: Scales (Jamtracks)")
      .fileName("Pentatonic2ScalesJamtracks")
      .pairs(ChordScaleAudio.pentatonicScales())
      .build(); 

  public static final Spec PENTATONIC_CHORDS = Spec.builder()
      .title("AnkiJazz Guitar - Pentatonics 4: Outline Chords (Jamtracks)")
      .fileName("Pentatonic4ChordsJamtracks")
      .pairs(ChordScaleAudio.pentatonicChords())
      .build(); 
  
  public FretboardJamCardGenerator(Spec spec, LoopIteratorFactory iteratorFactory) {
    this.spec = spec;
    this.iteratorFactory = iteratorFactory;
    this.numberOfSongs = spec.getPairs().size() * songsPerChordScalePair;
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

  interface MelodyProvider {
    void start(Scale scale);
    Part next();
  }
  
  class NoMelody implements MelodyProvider {

    @Override
    public void start(Scale scale) {
    }

    @Override
    public Part next() {
      return null;
    }
    
  }

  abstract class AbstractMelodyProvider implements MelodyProvider {
    private final int minPitch = 56;
    private final int maxPitch = minPitch + 24;
    private final int melodyVelocity = 110;
    private final NoteToMidiMapper mapper = NoteToMidiMapper.range(minPitch, maxPitch);
    private Iterator<Note> iterator;
    private final int midiNotesPerBar;
    private final int noteLengthDenominator;
    
    protected boolean ascending;
    protected Note prevNote;
    
    AbstractMelodyProvider(int midiNotesPerBar, int noteLengthDenominator) {
      this.midiNotesPerBar = midiNotesPerBar;
      this.noteLengthDenominator = noteLengthDenominator;
      reset();
    }

    @Override
    public void start(Scale scale) {
      maybeChangeDirection();
      List<Note> list = createNoteList(scale);
      iterator = Utils.loopIterator(list);
    }

    List<Note> createNoteList(Scale scale) {
      List<Note> list = scale.asList();
      if (!ascending) {
        Collections.reverse(list);
        Collections.rotate(list, 1);
      }
      rotateClosestToPrevNote(list);
      return list;
    }
    
    protected abstract void maybeChangeDirection();
    protected abstract void rotateClosestToPrevNote(List<Note> list);

    void reset() {
      if (ascending) {
        mapper.resetToLowest();
      } else {
        mapper.resetToHighest();
      }
    }
    
    @Override
    public Part next() {
      Sequential seq = Parts.seq();
      for (int i = 0; i < midiNotesPerBar; i++) {
        Note note = iterator.next();
        int pitch = ascending ? mapper.nextHigherUnbounded(note) : mapper.nextLowerUnbounded(note);
        seq.add(Parts.note(MELODY_MIDI_CHANNEL, pitch, melodyVelocity , noteLengthDenominator));
        seq.add(Parts.rest(noteLengthDenominator));
        prevNote = note;
      }
      return seq;
    }

  }
  
  class PlayOverChangesMelody extends AbstractMelodyProvider {

    PlayOverChangesMelody() {
      super(4, 4);
    }

    @Override
    protected void maybeChangeDirection() {
      ascending = !ascending;
      reset();
    }

    @Override
    protected void rotateClosestToPrevNote(List<Note> list) {
      
    }
    
  }
  class PlayThroughChangesMelody extends AbstractMelodyProvider {
    int directionCounter = -1;
    
    PlayThroughChangesMelody() {
      super(2, 2);
    }

    @Override
    protected void rotateClosestToPrevNote(List<Note> list) {
      if (directionCounter != 1) {
        return;
      }
      BiFunction<Note, Note, Integer> fn = ascending 
          ? (prev, next) -> (prev == next ? 100 : prev.semitones(next))
          : (prev, next) -> (prev == next ? 100 : next.semitones(prev));
      Note bestNote = prevNote;
      int bestValue = fn.apply(prevNote, prevNote); // = 100
      for (Note note : list) {
        if (fn.apply(prevNote, note) < bestValue) {
          bestNote = note;
          bestValue = fn.apply(prevNote, note);
        }
      }
      Collections.rotate(list, -list.indexOf(bestNote));
    }

    @Override
    protected void maybeChangeDirection() {
      directionCounter += 1;
      if (directionCounter == 2) {
        directionCounter = 0;
        ascending = !ascending;
        reset();
      }
    }

  }
  
  class SemitonesIteratorFactory  {
    private Iterator<Note> roots = iteratorFactory.iterator(Arrays.asList(Note.values()));
    private Interpolator numberOfChords;
    private Note prevRoot = roots.next();
    private final int minSemitones = 2;
    private final int maxSemitones = 5;
    SemitonesIteratorFactory() {
      numberOfChords = Utils.interpolator(0, numberOfSongs, minSemitones, maxSemitones);
    }

    public Iterator<Integer> create(Integer songIndex) {
      int chordsPerSong = numberOfChords.apply(songIndex);
      List<Note> notes = new ArrayList<>();
      do {
        for (int i = 0; i < chordsPerSong; i++) {
          Note root = roots.next();
          while (prevRoot.distance(root) < 2) {
            root = roots.next();
          }
          notes.add(root);
          prevRoot = root;
        }
      } while(getFirst(notes).distance(getLast(notes)) < 2);
      return Utils.loopIterator(notes.stream().map(n -> n.ordinal()).collect(toList()));
    }
  }
  
  class SongWrapperFactory implements Iterator<SongWrapper> {
    
    Iterator<ChordScaleAudio> pairs = iteratorFactory.iterator(spec.getPairs());
    MelodyProvider melody = new PlayOverChangesMelody();
    SemitonesIteratorFactory semitonesIteratorFactory = new SemitonesIteratorFactory();
    int songIndex = 0;
    
    @Override
    public SongWrapper next() {
      ChordScaleAudio pair = pairs.next();
      ScaleInfo scaleInfo = ScaleUniverse.MODES.findFirstOrElseThrow(pair.getScale());
      SongWrapper wrapper = SongWrapper.builder()
          .key("Mixed Keys")
          .progression(pair.getTitle() == null ? "Play ".concat(scaleInfo.getTypeName()) : pair.getTitle())
          .type(scaleInfo.getTypeName())
          .song(createSong(pair, songIndex))
          .comment(pair.getComment())
          .build();
      songIndex++;
      return wrapper;
    }
    
    private Song createSong(ChordScaleAudio pair, int songIndex) {
      Iterator<Integer> semitonesIterator = semitonesIteratorFactory.create(songIndex);
      List<Bar> bars = new ArrayList<>();
      for (int i = 0; i < context.getNumberOfBars() / numberOfBarsPerChord; i++) {
        int semitones = semitonesIterator.next();
        Scale transposedChord = pair.getAudio().transpose(semitones);
        Chord chord = new Chord(transposedChord, ScaleUniverse.CHORDS.findFirstOrElseThrow(transposedChord).getScaleName());
        Scale transposedScale = pair.getScale().transpose(semitones);
        melody.start(transposedScale);
        for (int j = 0; j < numberOfBarsPerChord; j++) {
          bars.add(Bar.of(melody.next(), chord));
        }
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
      cards.add(JamCard.builder()
          .instrument(Note.C)
          .context(context)
          .wrapper(wrapper)
          .ensembleSupplier(() -> latin(120))
          .position(position).build());
    }
    return cards;
  }

}
