package de.jlab.scales.anki;

import static de.jlab.scales.anki.AnkiUtils.ankiMp3;
import static de.jlab.scales.anki.AnkiUtils.ankiPng;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.base.Charsets;

import de.jlab.scales.Utils;
import de.jlab.scales.jtg.PngImageRenderer;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.HumanizingMidiOut;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.MidiOut;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.song.Ensemble;
import de.jlab.scales.midi.song.Song;
import de.jlab.scales.midi.song.SongWrapper;

public class JamCard implements Card {

  private final SongWrapper wrapper;
  private final Ensemble ensemble;
  private final RenderContext context;
  private final String assetId;
  private final Song song;

  public JamCard(RenderContext context, SongWrapper wrapper, Ensemble ensemble) {
    this.context = context;
    this.wrapper = wrapper;
    this.ensemble = ensemble;
    this.song = wrapper.getSong();
    this.assetId = computeAssetId();
  }

  @Override
  public double getDifficulty() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Map<String, Object> getJson() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("progression", getProgression());
    map.put("type", getType());
    map.put("key", getKey());
    map.put("style", getStyle());
    map.put("tempo", getTempo());
    map.put("songImage", getPngName());
    map.put("songAudio", getMp3Name());
    map.put("difficulty", getDifficulty());
    return map;
  }

  @Override
  public String getCsv() {
    return Stream.of(
        getProgression(),
        getType(),
        getKey(),
        getStyle(),
        Integer.toString(getTempo()),
        ankiPng(getPngName()),
        ankiMp3(getMp3Name())).collect(joining(CSV_DELIMITER));
  }
  
  
  @Override
  public void writeAssets(Path directory) {
    new PngImageRenderer(context, song).renderTo(directory.resolve(assetId.concat(".png")));
    Part part = ensemble.play(song, context.getRepeat());
    MidiOut mf = new HumanizingMidiOut(new MidiFile());
    part.perform(mf);
    mf.save(directory.resolve(assetId.concat(".midi")));
  }

  private String computeAssetId() {
    MidiFile midiFile = new MidiFile();
    Part part = ensemble.play(song, context.getRepeat());
    part.perform(midiFile);
    byte[] midiBytes = midiFile.getBytes();
    // songs in F# and Gb produce identical midi, so we need to add notation
    // same song may be played by multiple ensembles, so we need to add midi
    byte[] songBytes = song.toString().getBytes(Charsets.UTF_8);
    byte[] allBytes = new byte[midiBytes.length + songBytes.length];
    System.arraycopy(midiBytes, 0, allBytes, 0, midiBytes.length);
    System.arraycopy(songBytes, 0, allBytes, midiBytes.length, songBytes.length);
    return Utils.assetId(allBytes);
  }

  public String getAssetId() {
    return assetId;
  }
  
  public String getPngName() {
    return format("%s.png", assetId);
  }
  
  public String getMp3Name() {
    return format("%s.mp3",  assetId);
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
    return ensemble.getStyle();
  }

  public int getTempo() {
    return ensemble.getBpm();
  }
}
