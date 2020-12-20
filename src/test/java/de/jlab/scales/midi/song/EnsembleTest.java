package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.MidiUtils.midiPitchToNote;
import static de.jlab.scales.theory.Scales.Cm7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.jtg.PngImageRenderer;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.Drum;
import de.jlab.scales.midi.HumanizingMidiOut;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.MidiOut;
import de.jlab.scales.midi.MockMidiOut;
import de.jlab.scales.midi.NoteOn;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.Program;
import de.jlab.scales.midi.Tempo;
import de.jlab.scales.midi.TimeSignature;
import de.jlab.scales.theory.Note;

public class EnsembleTest {
  final TimeSignature timeSignature = Parts.timeSignature(3, 4);
  final Tempo tempo = Parts.tempo(93);
  private int lowestMidiPitch = 45;

  @Test
  public void testMonophonicInstrument() {
    Ensemble ensemble = new Ensemble(16, timeSignature, tempo);
    ensemble.monophonic(lowestMidiPitch, Program.AcousticBass, 80, -20).bar("x--- ..x.", 1, 5);
    MockMidiOut mock = new MockMidiOut();
    ensemble.play(song(), 1).perform(mock);
    assertEquals(4, mock.getNotes().size());
    assertNote(mock, 0, Note.C, 4);
    assertNote(mock, 1, Note.G, 1);
    assertNote(mock, 2, Note.D, 4);
    assertNote(mock, 3, Note.A, 1);
  }

  private void assertNote(MockMidiOut mock, int index, Note expectedNote, int expectedLength) {
    NoteOn noteOn = mock.getNotes().get(index);
    assertThat(midiPitchToNote(noteOn.getPitch())).isEqualTo(expectedNote);
    assertThat(noteOn.getNumerator()).isEqualTo(expectedLength);
  }

  @Test
  public void testPercussiveInstrument() {
    Ensemble ensemble = new Ensemble(16, timeSignature, tempo);
    int volume = 90;
    int pan = -20;
    ensemble.setDrumVolume(volume);
    ensemble.setDrumPan(pan);
    ensemble.percussive(Drum.Cowbell).bar("x...").bar("5...");

    Part part = ensemble.play(song(), 1);
    assertNotNull(part);
    MockMidiOut midiOut = new MockMidiOut();
    part.perform(midiOut);
    assertThat(midiOut.getNotes().size()).isEqualTo(2);
    assertThat(midiOut.getTimeSignature()).isEqualTo(timeSignature);
    assertThat(midiOut.getTempo()).isEqualTo(tempo);
    assertThat(midiOut.getVolume()).isEqualTo(volume);
    assertThat(midiOut.getPan()).isEqualTo(pan + 64);
  }

  private Song song() {
    return Song.of(
        Bar.of(Chord.of(Cm7, "Cm7")), Bar.of(Chord.of(Cm7.transpose(2), "Dm7")));
  }
  Path dir = Paths.get("build", "EnsembleTest");

  @Test
  public void testGrooves() throws IOException {
    RenderContext ctx = RenderContext.ANKI;
    Files.createDirectories(dir);
    Song song = MidiTestUtils.createRandomSong(ctx.getNumberOfBars());
    new PngImageRenderer(ctx, song).renderTo(dir.resolve("song.png"));
    groove(song, Ensembles.latin(120), dir.resolve("latin.midi"));
    groove(song, Ensembles.funk(80), dir.resolve("funk.midi"));
  }
  
  @Test
  public void test2ChordsPerBar() {
    groove(MidiTestUtils.songWith2ChordPerBar(), Ensembles.funk(85), dir.resolve("funk2.midi"));
    groove(MidiTestUtils.songWith2ChordPerBar(), Ensembles.latin(120), dir.resolve("latin2.midi"));
  }

  private void groove(Song song, Ensemble ensemble, Path path) {
    Part part = ensemble.play(song, 4);
    MidiOut mf = new HumanizingMidiOut(new MidiFile());
    part.perform(mf);
    mf.save(path);
  }
}
