package com.ankijazz.midi.song;

import static com.ankijazz.midi.MidiUtils.midiPitchToNote;
import static com.ankijazz.midi.song.MelodyInstrument.MELODY_MIDI_CHANNEL;
import static com.ankijazz.midi.song.MidiTestUtils.song;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ankijazz.TestUtils;
import com.ankijazz.midi.Drum;
import com.ankijazz.midi.MockMidiOut;
import com.ankijazz.midi.NoteOn;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.Program;
import com.ankijazz.midi.Tempo;
import com.ankijazz.midi.TimeSignature;
import com.ankijazz.theory.Note;
import com.ankijazz.theory.Scales;

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
  
  @Test
  public void testMelody() {
    // part length differs from bar length!
    Part m1 = Parts.seq(Parts.note(MELODY_MIDI_CHANNEL, 60, 127, 1, 2), Parts.rest(1, 4));
    Part m2 = Parts.note(MELODY_MIDI_CHANNEL, 62, 127, 1, 2);
    Bar b1 = Bar.of(m1, Chord.of(Scales.Cm7, "Cm7"));
    Bar b2 = Bar.of(m2, Chord.of(Scales.Cm7.transpose(Note.D), "Dm7"));
    Song song = Song.of(b1, b2);
    Part latin = Ensembles.latin(125).play(song, 2);
    TestUtils.assertMidiMatches(latin, getClass(), "latinWithMelody.midi");
  }
  
}
