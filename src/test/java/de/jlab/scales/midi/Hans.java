package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Scales.Cmaj7;

import java.util.List;

import org.junit.Test;

import de.jlab.scales.theory.BackCycler;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class Hans extends Base16 {
  MidiFile mf = new MidiFile();
  

  @Test
  public void testHans() {
    Note[] melody = {G, E, E, E, F, D, D, D, C, D, E, F, G, G, G, G, G, E, E, E, F, D, D, D, C, E, G, G};
    Scale targetChord = Cmaj7.transpose(1);
    BackCycler bc = new BackCycler(targetChord);
    bc.seq(melody);
    List<Scale> chords = bc.backcycle();
    
    mf.setTempo(60);
    for (int i = 0; i < melody.length; i++) {
      bar(melody[i], chords.get(i));
    }
    bar(Note.C, targetChord);
    mf.save("temp/hans.mid");
    
  }

  private void bar(Note meldy, Scale chord) {
      note(1, 72 + meldy.ordinal(), 127, 4).perform(mf);
      note(2, 36 + chord.getRoot().ordinal(), 100, 4).perform(mf);
      playDrop2VoicingOfChord(chord);
      rest(4).perform(mf);
  }

  private void playDrop2VoicingOfChord(Scale chord) {
    int i = 0; 
    for (Note note : chord.asSet()) {
      int pitch = 60 + note.ordinal();
      if (i == 2)
        pitch -= 12; 
      note(3, pitch, 64, 4).perform(mf);
      i++;
    }
  }


}
