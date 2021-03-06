package com.ankijazz.anki;

import static com.ankijazz.anki.AnkiUtils.ankiMp3;
import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.ankijazz.Utils;
import com.ankijazz.midi.MidiFile;
import com.ankijazz.midi.MidiOut;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.song.Bar;
import com.ankijazz.midi.song.Chord;
import com.ankijazz.midi.song.Ensembles;
import com.ankijazz.midi.song.MelodyInstrument;
import com.ankijazz.midi.song.NoteToMidiMapper;
import com.ankijazz.midi.song.Song;
import com.ankijazz.theory.DegreeParser.Degrees;
import com.ankijazz.theory.KeySignature;
import com.ankijazz.theory.Note;

@lombok.Builder
public class ChordNoteTrainerCard implements Card {

  private final Degrees degrees;
  private final KeySignature keySignature;
  private final Note root;
  private final int index;
  private final String typeName;
  private final String assetId = Utils.uuid();

  @Override
  public double getDifficulty() {
    return keySignature.getNumberOfAccidentals() / 6.0;
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
    return Parts.note(MelodyInstrument.MELODY_MIDI_CHANNEL, midiPitch, 100, 1);
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
  
  public String getTypeName() {
    return typeName;
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
    return Stream.of(
        getIntervalName(),
        getChordName(),
        getNoteName(),
        getTypeName(),
        ankiMp3(getFrontMp3Name()),
        ankiMp3(getBackMp3Name())).collect(joining(CSV_DELIMITER));
  }

  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("intervalName", getIntervalName());
    map.put("chordName", getChordName());
    map.put("noteName", getNoteName());
    map.put("typeName", getTypeName());
    map.put("frontMp3Name", getFrontMp3Name());
    map.put("backMp3Name", getBackMp3Name());
    return map;
  }

}
