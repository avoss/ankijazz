package de.jlab.scales.fretboard2;

import static de.jlab.scales.fretboard2.Marker.BACKGROUND;
import static de.jlab.scales.fretboard2.Marker.FOREGROUND;

import java.util.List;

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
  private static final int FRETBOARD_MIDI_VELOCITY = 110;
  private static final int TEMPO = 40;
  
  private final Fretboard fretboard;
  private final boolean foregroundIncludesRoot;
  private final boolean renderForeground;
  private final boolean renderBackground;
  private final Scale backgroundChord;
  private final Counter counter;
  private final int TICK = 16;
  private final Part REST = Parts.rest(1, TICK);

  @lombok.Builder
  public MidiFretboardRenderer(Fretboard fretboard, Scale backgroundChord, boolean renderForeground, boolean foregroundIncludesRoot, boolean renderBackground) {
    this.fretboard = fretboard;
    this.backgroundChord = backgroundChord;
    this.renderForeground = renderForeground;
    this.foregroundIncludesRoot = foregroundIncludesRoot;
    this.renderBackground = renderBackground;
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
      int foreground = renderForeground ? numForeground + (foregroundIncludesRoot ? numRoot : 0) : 0;
      int background = renderBackground ? numBackground + numForeground + numRoot : 0;
      return background + foreground;
    }

    public int getSongDuration() {
      return getNumberOfNotes() + (renderForeground ? 4 : 0) + (renderBackground ? 4 : 0);
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
    if (renderBackground) {
      fretboard.add(new MidiRenderer(BACKGROUND).render());
    }
    if (renderForeground) {
      fretboard.add(new MidiRenderer(FOREGROUND).render());
    }
    par.add(fretboard);
    Sequential seq = Parts.seq(Parts.tempo(TEMPO), REST, par);
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
        seq.add(Parts.note(FRETBOARD_MIDI_CHANNEL, previousPitch, FRETBOARD_MIDI_VELOCITY, duration, TICK));
        seq.add(Parts.rest(duration, TICK));
      }
      previousPitch = stringPitch + fret;
    }

  }

  private Part renderBackgroundChord() {
    Sequential seq = Parts.seq();
    if (backgroundChord == null) {
      return seq;
    }
    int duration = counter.getSongDuration();
    seq.add(Parts.program(BG_CHORD_MIDI_CHANNEL, Program.Pad2_warm));
    seq.add(REST);
    Drop2ChordGenerator g = new Drop2ChordGenerator(90);
    int[] midiChord = g.midiChord(backgroundChord);
    for (int i = 0; i < midiChord.length; i++) {
      seq.add(Parts.note(BG_CHORD_MIDI_CHANNEL, midiChord[i], 40, duration, TICK));
    }
    int bassPitch = MidiUtils.noteToMidiPitchBelowOrSame(45, backgroundChord.getRoot());
    seq.add(Parts.note(BG_CHORD_MIDI_CHANNEL, bassPitch, 90, duration, TICK));
    return seq;
  }

}
