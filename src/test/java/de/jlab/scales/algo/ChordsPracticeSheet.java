package de.jlab.scales.algo;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.program;
import static de.jlab.scales.midi.Parts.rest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.Parallel;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Program;
import de.jlab.scales.midi.Sequential;
import de.jlab.scales.theory.Accidental;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;

public class ChordsPracticeSheet {

  private static final int CHORD_CHANNEL = 3;
  private static final int BASS_CHANNEL = 2;
  private static final int SECTION_REPEAT_COUNT = 2;
  private static final int SECTION_LENGTH = 4;
  
  // String[] qualities = {"", "m", "m7", "m7b5", "7", "7b9", "7#5", "6", "9",
  // "m9", "Maj7", "Maj9", "Sus4", "Dim", "Aug", "Dim7"};
  String[] qualities = { "m7", "m7b5", "7", "7b9", "7#9", "Maj7", "Sus47", "7#5",  "7b5", "6", "m6", "79", "m79", "Maj79", "mMaj7", "Maj7#5", "Maj7#11"};
  // String[] qualities = { "6", "m6" };

  Random random = new Random();

  abstract class SectionAware<T> {
    protected int positionInSection;
    protected int globalPosition;
    void add(T element) {
      positionInSection += 1;
      globalPosition += 1;
      if (positionInSection == 1) {
        onSectionStart(element);
      }
      onElement(element);
      if (positionInSection == SECTION_LENGTH) {
        positionInSection = 0;
        onSectionEnd();
      }
    }
    protected abstract void onSectionStart(T element);
    protected abstract void onElement(T element);
    protected abstract void onSectionEnd();
  }
  
  @Test
  public void printCombinedSheet() throws IOException {
    createSheet("temp/combined", this.qualities);
  }
  
  @Test
  public void simple() throws IOException {
    createSheet("temp/medium", "m7", "7", "Maj7", "m7b5", "6", "m6", "Sus47", "7b9", "7#5", "7b5", "79", "Maj79", "m79");
    createSheet("temp/all", 
        "mMaj7", "m7", "m79", "m6", "m7b5",             // minor 
        "Maj7", "Maj79", "6", "69",                     // major
        "7", "79", "Sus47", "713",                      // dominant
        "7b9", "7#9", "7b5", "7#5");                    // altered
  }

  @Test
  public void printSingleSheets() throws IOException {
    for (String quality : qualities)
      createSheet("temp/" + quality, quality);
  }

  private void createSheet(String name, String ... qualities) throws IOException {
    MidiFile mf = new MidiFile();
    mf.setTempo(60);
    Sequential seq = new Sequential();
    
    seq.add(program(BASS_CHANNEL, Program.FretlessBass));
    seq.add(program(CHORD_CHANNEL, Program.ElectricPiano1));

    int N = 160;
    RandomChordGenerator chordGenerator = new RandomChordGenerator().qualities(qualities);
    List<Scale> chords = Lists.newArrayList();
    for (int i = 0; i < N; i++)
      chords.add(chordGenerator.next());
    print(name, chords);
    seq.add(play(chords));
    
    seq.perform(mf);
    mf.save(name + ".mid");
  }

  class ChordPlayer extends SectionAware<Scale> {
    private Sequential result = new Sequential();
    private Sequential section;
    @Override
    protected void onSectionStart(Scale chord) {
      section = new Sequential();
    }
    @Override
    protected void onElement(Scale chord) {
      Part part = play(chord);
//      Accidental accidental = random.nextBoolean() ? Accidental.FLAT : Accidental.SHARP;
//      section.add(lyrics(chord.asChord(accidental)));
      for (int i = 0; i < 4; i++) {
        section.add(part);
        section.add(rest(4));
      }
    }
    @Override
    protected void onSectionEnd() {
      result.add(section.repeat(SECTION_REPEAT_COUNT));
    }
    
    public Part toPart() {
      return result;
    }
    
    private Parallel play(Scale chord) {
      Parallel par = new Parallel();
      par.add(playBass(chord));
      par.add(playChord(chord));
      return par;
    }

    private Part playBass(Scale chord) {
      return note(BASS_CHANNEL, 36 + chord.getRoot().ordinal(), 100, 4);
    }

    private Part playChord(Scale chord) {
      Parallel par = new Parallel();
      for (Note note : chord.asSet()) {
        int pitch = 60 + note.ordinal();
       // pitch = random.nextBoolean() ? pitch + 12 : pitch;
        par.add(note(CHORD_CHANNEL, pitch, 80, 4));
      }
      return par;
    }
  }

  private Part play(List<Scale> chords) {
    ChordPlayer player = new ChordPlayer();
    for (Scale chord : chords) {
      player.add(chord);
    }
    return player.toPart();
  }


  class SheetPrinter extends SectionAware<Scale> {
    StringBuilder sb = new StringBuilder();
    @Override
    protected void onSectionStart(Scale element) {
      sb.append(String.format("%3d", printPosition(globalPosition)));
    }
    
    private int printPosition(int globalPosition) {
      return 1 + (globalPosition - 1) * SECTION_REPEAT_COUNT;
    }

    @Override
    protected void onElement(Scale chord) {
      Accidental accidental = random.nextBoolean() ? Accidental.FLAT : Accidental.SHARP;
      sb.append(String.format("| %8s ", chord.asChord(accidental)));
    }
    
    @Override
    protected void onSectionEnd() {
      sb.append("|\n");
    }
    @Override
    public String toString() {
      return sb.toString();
    }
  }
  
  private void print(String name, List<Scale> chords) throws IOException {
    SheetPrinter printer = new SheetPrinter();
    for (Scale chord : chords) {
      printer.add(chord);
    }
    Files.write(printer.toString(), new File(name + ".txt"), Charsets.UTF_8);
    System.out.println(printer.toString());
  }
  
}
