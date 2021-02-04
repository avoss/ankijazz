package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.MidiUtils.midiPitchToNote;
import static de.jlab.scales.midi.song.MidiTestUtils.song;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import de.jlab.scales.TestUtils;
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
    Ensemble ensemble = new Ensemble("Test", 16, timeSignature, tempo);
    ensemble.monophonic(lowestMidiPitch, Program.AcousticBass, 80, -20).bar("x--- ..x.", 1, 5);
    MockMidiOut mock = new MockMidiOut();
    ensemble.play(song("Cm7 Dm7"), 1).perform(mock);
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
    Ensemble ensemble = new Ensemble("Test", 16, timeSignature, tempo);
    int volume = 90;
    int pan = -20;
    ensemble.setDrumVolume(volume);
    ensemble.setDrumPan(pan);
    ensemble.percussive(Drum.Cowbell).bar("x...").bar("5...");

    Part part = ensemble.play(song("Cm7 Dm7"), 1);
    assertNotNull(part);
    MockMidiOut midiOut = new MockMidiOut();
    part.perform(midiOut);
    assertThat(midiOut.getNotes().size()).isEqualTo(2);
    assertThat(midiOut.getTimeSignature()).isEqualTo(timeSignature);
    assertThat(midiOut.getTempo()).isEqualTo(tempo);
    assertThat(midiOut.getVolume()).isEqualTo(volume);
    assertThat(midiOut.getPan()).isEqualTo(pan + 64);
  }

  @Test
  public void testMidi() {
    Song song = song("Em7 Am7 D7 Cmaj7   Fmaj7 Bm7b5 E7#5 Am6");
    Part latin = Ensembles.latin(125).play(song, 2);
    TestUtils.assertMidiMatches(latin, getClass(), "latin.midi");
    Part funk = Ensembles.funk(90).play(song, 2);
    TestUtils.assertMidiMatches(funk, getClass(), "funk.midi");
  }
  
}
