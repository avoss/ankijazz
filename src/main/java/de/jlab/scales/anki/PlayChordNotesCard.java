package de.jlab.scales.anki;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import de.jlab.scales.Utils;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.MidiOut;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.Parts;
import de.jlab.scales.midi.song.Bar;
import de.jlab.scales.midi.song.Chord;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.MelodyInstrument;
import de.jlab.scales.midi.song.NoteToMidiMapper;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.theory.DegreeParser.Degrees;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;

@lombok.Builder
public class PlayChordNotesCard implements Card {

  private final Degrees degrees;
  private final KeySignature keySignature;
  private final Note root;
  private final int index;
  private final String typeName;
  private final String assetId = Utils.uuid();

  @Override
  public double getDifficulty() {
    return 0;
  }

  @Override
  public String getAssetId() {
    return assetId;
  }

  enum Side { 
    FRONT, BACK;
    String code() {
      return name().substring(0, 1);
    }
  }
  
  @Override
  public void writeAssets(Path directory) {
    writeMidi(directory, Side.FRONT);
    writeMidi(directory, Side.BACK);
  }

  private void writeMidi(Path directory, Side side) {
    Song song = makeSong(side);
    Part part = Ensembles.chordOnly(60).play(song, 1);
    MidiOut mf = new MidiFile();
    part.perform(mf);
    mf.save(midiPath(directory, side));
  }

  private Song makeSong(Side side) {
    Chord chord = new Chord(degrees.asScale(root), "");
    Part melody = createMelody(side);
    Bar bar = new Bar(melody, List.of(chord));
    return new Song(List.of(bar));
  }

  private Part createMelody(Side side) {
    if (side == Side.FRONT) {
      return Parts.rest(1);
    }
    Note note = degrees.asList(root).get(index);
    int midiPitch = NoteToMidiMapper.octave(64).nextClosest(note);
    return Parts.note(MelodyInstrument.MELODY_MIDI_CHANNEL, midiPitch, 70, 1);
  }

  private Path midiPath(Path directory, Side side) {
    return directory.resolve(assetId.concat(side.code()).concat(".midi"));
  }
  
  public String getIntervalName() {
    return Integer.toString(noteIndexInScale() + 1);
  }
  
  public String getChordName() {
    return keySignature.notate(root) + typeName;
  }
  
  public String getNoteName() {
    return keySignature.notate(degrees.asList(root).get(index));
  }
  
  private int noteIndexInScale() {
    return degrees.getDegrees().get(index).getNoteIndexInScale();
  }
  
  public String getFrontMp3Name() {
    return assetId.concat(Side.FRONT.code()).concat(".mp3");
  }
  
  public String getBackMp3Name() {
    return assetId.concat(Side.BACK.code()).concat(".mp3");
  }

  @Override
  public String getCsv() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Object> getJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
