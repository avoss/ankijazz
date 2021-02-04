package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.Marker.BACKGROUND;
import static de.jlab.scales.fretboard2.Marker.FOREGROUND;

import java.util.List;

import com.google.common.base.Optional;

import de.jlab.scales.midi.MidiUtils;
import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.midi.song.Drop2ChordGenerator;
import de.jlab.scales.theory.Scale;

@lombok.Getter
public class MidiFretboardRenderer implements FretboardRenderer<Part> {
  private static final int BG_CHORD_MIDI_CHANNEL = 0;
  private static final int FRETBOARD_MIDI_CHANNEL = 1;

  private final Fretboard fretboard;
  private final boolean foregroundIncludesRoot;
  private final Optional<Scale> backgroundChord;
  private final Counter counter;
  private final int TICK = 16;
  private final Part REST = Parts.rest(1, TICK);

  public MidiFretboardRenderer(Fretboard fretboard) {
    this(fretboard, true, Optional.absent());
  }

  public MidiFretboardRenderer(Fretboard fretboard, boolean foregroundIncludesRoot, Scale backgroundChord) {
    this(fretboard, foregroundIncludesRoot, Optional.of(backgroundChord));
  }

  private MidiFretboardRenderer(Fretboard fretboard, boolean foregroundIncludesRoot, Optional<Scale> backgroundChord) {
    this.fretboard = fretboard;
    this.foregroundIncludesRoot = foregroundIncludesRoot;
    this.backgroundChord = backgroundChord;
    this.counter = countNotes();
  }

  @lombok.Data
  class Counter implements MarkerRenderer {
    int numForeground;
    int numBackground;
    int numRoot;

    @Override
    public void renderEmpty(GuitarString string, int fret) {
      // ignore
    }

    @Override
    public void renderForeground(GuitarString string, int fret) {
      numForeground += 1;
    }

    @Override
    public void renderBackground(GuitarString string, int fret) {
      numBackground += 1;
    }

    @Override
    public void renderRoot(GuitarString string, int fret) {
      numRoot += 1;
    }

    int getNumberOfNotes() {
      int background = isBackgroundPresent() ? numBackground + numForeground + numRoot : 0;
      int foreground = isForegroundPresent() ? numForeground + (foregroundIncludesRoot ? numRoot : 0) : 0;
      return background + foreground;
    }

    boolean isForegroundPresent() {
      return numForeground > 0;
    }

    boolean isBackgroundPresent() {
      return numBackground > 0;
    }

    public int getSongDuration() {
      return getNumberOfNotes() + (isForegroundPresent() ? 4 : 0) + (isBackgroundPresent() ? 4 : 0);
    }

  }

  private Counter countNotes() {
    Counter counter = new Counter();
    render(counter);
    return counter;
  }

  public void render(MarkerRenderer renderer) {
    for (GuitarString string : fretboard.getStrings()) {
      string.apply(renderer);
    }
  }

  @Override
  public Part render() {
    Parallel par = Parts.par();
    par.add(renderBackgroundChord());
    Sequential fretboard = Parts.seq();
    if (counter.isBackgroundPresent()) {
      fretboard.add(new MidiRenderer(BACKGROUND).render());
    }
    if (counter.isForegroundPresent()) {
      fretboard.add(new MidiRenderer(FOREGROUND).render());
    }
    par.add(fretboard);
    Sequential seq = Parts.seq(Parts.tempo(30), REST, par);
    return seq;
  }

  class MidiRenderer implements MarkerRenderer {

    private int stringPitch;
    private Sequential seq = Parts.seq();
    private Marker what;
    private int previousPitch;

    public MidiRenderer(Marker what) {
      this.what = what;
    }

    public Part render() {
      seq.add(Parts.program(FRETBOARD_MIDI_CHANNEL, Program.ElectricPiano1));
      seq.add(REST);
      
      List<Integer> stringPitches = fretboard.getTuning().getMidiPitches();
      for (int i = 0; i < stringPitches.size(); i++) {
        stringPitch = stringPitches.get(i);
        fretboard.getString(i).apply(this);
      }
      play(0, 4);
      return seq;
    }

    @Override
    public void renderEmpty(GuitarString string, int fret) {
      // ignore
    }

    @Override
    public void renderForeground(GuitarString string, int fret) {
      play(fret, 1);
    }

    @Override
    public void renderBackground(GuitarString string, int fret) {
      if (what == BACKGROUND) {
        play(fret, 1);
      }
    }

    @Override
    public void renderRoot(GuitarString string, int fret) {
      if (what == BACKGROUND || foregroundIncludesRoot) {
        play(fret, 1);
      }
    }

    private void play(int fret, int duration) {
      if (previousPitch > 0) {
        seq.add(Parts.note(FRETBOARD_MIDI_CHANNEL, previousPitch, 100, duration, TICK));
        seq.add(Parts.rest(duration, TICK));
      }
      previousPitch = stringPitch + fret;
    }

  }

  private Part renderBackgroundChord() {
    Sequential seq = Parts.seq();
    if (!backgroundChord.isPresent()) {
      return seq;
    }
    int duration = counter.getSongDuration();
    seq.add(Parts.program(BG_CHORD_MIDI_CHANNEL, Program.Pad2_warm));
    seq.add(REST);
    Drop2ChordGenerator g = new Drop2ChordGenerator(90);
    int[] midiChord = g.midiChord(backgroundChord.get());
    for (int i = 0; i < midiChord.length; i++) {
      seq.add(Parts.note(BG_CHORD_MIDI_CHANNEL, midiChord[i], 50, duration, TICK));
    }
    int bassPitch = MidiUtils.noteToMidiPitchBelow(45, backgroundChord.get().getRoot());
    seq.add(Parts.note(BG_CHORD_MIDI_CHANNEL, bassPitch, 70, duration, TICK));
    return seq;
  }

}
