package com.ankijazz.anki;

import static com.ankijazz.anki.AnkiUtils.ankiMp3;
import static com.ankijazz.anki.AnkiUtils.ankiPng;
import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.ankijazz.Utils;
import com.ankijazz.difficulty.Difficulties;
import com.ankijazz.difficulty.DifficultyModel;
import com.ankijazz.midi.MidiFile;
import com.ankijazz.midi.MidiOut;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.TransposingMidiOut;
import com.ankijazz.midi.song.Ensemble;
import com.ankijazz.midi.song.Song;
import com.ankijazz.midi.song.SongWrapper;
import com.ankijazz.sheet.PngImageRenderer;
import com.ankijazz.sheet.RenderContext;
import com.ankijazz.theory.Note;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;

public class JamCard implements Card {

  private final SongWrapper wrapper;
  private final Supplier<Ensemble> ensembleSupplier;
  private final RenderContext context;
  private final String assetId;
  private final Song song;
  private final Note instrument;
  private final Optional<FretboardPosition> position;
  private final double difficulty;
  private final int bpm;
  private final String style;
  private final String stringSet;
  
  @lombok.Builder
  private JamCard(Note instrument, RenderContext context, SongWrapper wrapper, Supplier<Ensemble> ensembleSupplier, FretboardPosition position, String stringSet) {
    this.instrument = instrument;
    this.context = context;
    this.wrapper = wrapper;
    this.ensembleSupplier = ensembleSupplier;
    this.stringSet = stringSet;
    this.position = Optional.fromNullable(position);
    this.song = wrapper.getSong();
    this.assetId = computeAssetId();
    this.difficulty = computeDifficulty();
    Ensemble ensemble = ensembleSupplier.get();
    this.bpm = ensemble.getBpm();
    this.style = ensemble.getStyle();
  }


  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    double songDifficulty = Difficulties.getSongDifficulty(wrapper.getSong());
    model.doubleTerm(100).update(songDifficulty);
    model.doubleTerm(60, 140, 50).update(bpm);
    double difficulty = model.getDifficulty();
    return difficulty;
  }

  @Override
  public double getDifficulty() {
    return difficulty;
  }

  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("title", getProgression());
    map.put("comment", getComment());
    map.put("type", getType());
    map.put("key", getKey());
    map.put("style", getStyle());
    map.put("tempo", getTempo());
    map.put("songPng", getPngName());
    map.put("songMp3", getMp3Name());
    map.put("difficulty", getDifficulty());
    if (position.isPresent()) {
      map.put("positionLabel", position.get().getLabel());
      map.put("positionImage", position.get().getImage());
      map.put("stringSet", getStringSet());
    }
    return map;
  }

  @Override
  public String getCsv() {
    Stream<String> positionStream = position.isPresent() 
        ? Stream.of(position.get().getLabel(), ankiPng(position.get().getImage()), getStringSet()) 
        : Stream.empty();
    Stream<String> songStream = Stream.of(
        getProgression(),
        getComment(),
        getType(),
        getKey(),
        getStyle(),
        Integer.toString(getTempo()),
        ankiPng(getPngName()),
        ankiMp3(getMp3Name()));
    return Stream.concat(songStream, positionStream).collect(joining(CSV_DELIMITER));
  }
  
  
  @Override
  public void writeAssets(Path directory) {
    new PngImageRenderer(context, song).renderTo(pngPath(directory));
    Part part = ensembleSupplier.get().play(song, context.getRepeat());
    MidiOut mf = new TransposingMidiOut(new MidiFile(), instrument);
    part.perform(mf);
    mf.save(midiPath(directory));
  }

  private Path midiPath(Path directory) {
    return directory.resolve(assetId.concat(".midi"));
  }

  private Path pngPath(Path directory) {
    return directory.resolve(getPngName());
  }

  private String computeAssetId() {
    // songs in F# and Gb produce identical midi, so we need to add notation
    // same song may be played by multiple ensembles, so we need to add midi
    MidiFile midiFile = new MidiFile();
    MidiOut midiOut = new TransposingMidiOut(midiFile, instrument);
    Part part = ensembleSupplier.get().play(song, context.getRepeat());
    part.perform(midiOut);
    byte[] midiBytes = midiFile.getBytes();
    byte[] songBytes = song.toString().getBytes(Charsets.UTF_8);
    return Utils.assetId(midiBytes, songBytes);
  }

  @Override
  public String getAssetId() {
    return assetId;
  }
  
  public String getPngName() {
    return assetId.concat(".png");
  }
  
  public String getMp3Name() {
    return assetId.concat(".mp3");
  }

  public String getKey() {
    return wrapper.getKey();
  }

  public String getProgression() {
    return wrapper.getProgression();
  }

  public String getType() {
    return wrapper.getType();
  }

  public String getStyle() {
    return style;
  }

  public int getTempo() {
    return bpm;
  }
  
  public boolean isWithGuitar() {
    return position.isPresent();
  }
  
  public FretboardPosition getPosition() {
    return position.get();
  }

  public String getStringSet() {
    return stringSet == null ? "" : stringSet;
  }
  
  public String getComment() {
    return wrapper.getComment();
  }
}
