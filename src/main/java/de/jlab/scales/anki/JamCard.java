package de.jlab.scales.anki;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

import java.nio.file.Path;
import java.util.Map;

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

public class JamCard implements Card {

  private Song song;
  private Ensemble ensemble;
  private RenderContext context;
  private String assetId;

  public JamCard(RenderContext context, Song song, Ensemble ensemble) {
    this.context = context;
    this.song = song;
    this.ensemble = ensemble;
    this.assetId = computeAssetId();
  }

  @Override
  public double getDifficulty() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Map<String, Object> getJson() {
    // TODO Auto-generated method stub
    return emptyMap();
  }

  @Override
  public String getCsv() {
    // TODO Auto-generated method stub
    return "TODO";
  }
  
  public String getPngName() {
    return format("%s.png", assetId);
  }

  public String getMp3Name() {
    return format("%s.mp3",  assetId);
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
}
