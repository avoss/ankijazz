package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;
import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;

import de.jlab.scales.Utils;
import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.jtg.PngImageRenderer;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.MidiOut;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.TransposingMidiOut;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongDifficultyModel;
import de.jlab.scales.midi.song.SongWrapper;
import de.jlab.scales.theory.Note;

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
  
  public JamCard(Note instrument, RenderContext context, SongWrapper wrapper, Supplier<Ensemble> ensemble, FretboardPosition position) {
    this(instrument, context, wrapper, ensemble, Optional.of(position));
  }
  
  public JamCard(Note instrument, RenderContext context, SongWrapper wrapper, Supplier<Ensemble> ensemble) {
    this(instrument, context, wrapper, ensemble, Optional.absent());
  }
  
  public JamCard(Note instrument, RenderContext context, SongWrapper wrapper, Supplier<Ensemble> ensembleSupplier, Optional<FretboardPosition> position) {
    this.instrument = instrument;
    this.context = context;
    this.wrapper = wrapper;
    this.ensembleSupplier = ensembleSupplier;
    this.position = position;
    this.song = wrapper.getSong();
    this.assetId = computeAssetId();
    this.difficulty = computeDifficulty();
    Ensemble ensemble = ensembleSupplier.get();
    this.bpm = ensemble.getBpm();
    this.style = ensemble.getStyle();
  }


  private double computeDifficulty() {
    DifficultyModel model = new DifficultyModel();
    double songDifficulty = new SongDifficultyModel().getDifficulty(wrapper.getSong());
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
    }
    return map;
  }

  @Override
  public String getCsv() {
    Stream<String> positionStream = position.isPresent() ? Stream.of(position.get().getLabel(), ankiPng(position.get().getImage())) : Stream.empty();
    Stream<String> songStream = Stream.of(
        getProgression(),
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
    return wrapper.getProgressionSet();
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

}
