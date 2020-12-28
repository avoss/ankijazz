package de.jlab.scales.midi.song;

import static de.jlab.scales.TestUtils.majorKeySignature;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Debug;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Major6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.Minor6251;
import static de.jlab.scales.midi.song.SongFactory.Feature.SeventhChords;
import static de.jlab.scales.midi.song.SongFactory.Feature.Triads;
import static de.jlab.scales.midi.song.SongFactory.Feature.WithSubs;
import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.Gb;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.jlab.scales.jtg.PngImageRenderer;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.HumanizingMidiOut;
import de.jlab.scales.midi.MidiFile;
import de.jlab.scales.midi.MidiOut;
import de.jlab.scales.midi.Part;
import de.jlab.scales.midi.song.SongFactory.Feature;
import de.jlab.scales.midi.song.SongFactory.Major6251;
import de.jlab.scales.midi.song.SongFactory.Minor6251;
import de.jlab.scales.midi.song.SongFactory.ProgressionFactory;
import de.jlab.scales.theory.KeySignature;

public class SongFactoryTest {

  @Test
  public void testMajor6251() {
    ProgressionFactory factory = new Major6251(EnumSet.of(SeventhChords));
    assertProgression(factory, majorKeySignature(C), "Am7", "Dm7", "G7", "CΔ7");
    assertProgression(factory, majorKeySignature(Gb, FLAT), "Ebm7", "Abm7", "Db7", "GbΔ7");
    assertProgression(factory, majorKeySignature(Gb, SHARP), "D#m7", "G#m7", "C#7", "F#Δ7");
  }

  @Test
  public void testMinor6251() {
    ProgressionFactory factory = new Minor6251(EnumSet.of(SeventhChords));
    assertProgression(factory, majorKeySignature(C), "FΔ7", "Bm7b5", "E7b9", "Am7");
    assertProgression(factory, majorKeySignature(Gb, FLAT), "CbΔ7", "Fm7b5", "Bb7b9", "Ebm7");
    assertProgression(factory, majorKeySignature(Gb, SHARP), "BΔ7", "E#m7b5", "A#7b9", "D#m7");
  }
  
  private void assertProgression(ProgressionFactory factory, KeySignature keySignature, String ... expected) {
    List<String> actual = factory.create(keySignature).stream().map(bar -> bar.getChords().get(0)).map(c -> c.getSymbol()).collect(toList());
    assertEquals(List.of(expected), actual);
  }
  
  @Test
  public void testNoDuplicates() {
    SongFactory factory = new SongFactory(EnumSet.of(Debug, SeventhChords, Minor6251));
    List<Song> songs = factory.generate(16);
    assertEquals(2, songs.size());
    assertNotEquals(songs.get(0), songs.get(1));
  }
  
  @Test
  public void testKeys() {
    assertNumberOfSongs(EnumSet.of(SeventhChords, Major6251, EachKey), 13);
    assertNumberOfSongs(EnumSet.of(Triads, Major6251, AllKeys), 4);
  }

  private void assertNumberOfSongs(EnumSet<Feature> features, int expectedNumberOfSongs) {
    SongFactory factory = new SongFactory(features);
    List<Song> songs = factory.generate(16);
    assertEquals(expectedNumberOfSongs, songs.size());
  }

  @Test
  public void assertNoDuplicateSongsAreGenerated() {
    EnumSet<Feature> features = EnumSet.of(SeventhChords, WithSubs,  Major6251, AllKeys, EachKey);
    SongFactory factory = new SongFactory(features);
    RenderContext context = RenderContext.ANKI;
    List<Song> list = factory.generate(context.getNumberOfBars()).stream().collect(toList());
    Set<Song> set = factory.generate(context.getNumberOfBars()).stream().collect(toSet());
    assertEquals(list.size(), set.size());
  }
    
  
  @Test
  public void testRenderSongs() throws IOException {
    RenderContext ctx = RenderContext.ANKI;
    Path dir = Paths.get("build", "SongFactoryTest");
    Files.createDirectories(dir);
    SongFactory songFactory = new SongFactory(EnumSet.of(SeventhChords, Major6251, Minor6251, AllKeys));
    int songIndex = 0;
    for (Song song: songFactory.generate(ctx.getNumberOfBars())) {
      String name = String.format("song%02d", songIndex++);
      new PngImageRenderer(ctx, song).renderTo(dir.resolve(name.concat(".png")));
      groove(song, Ensembles.latin(120), dir.resolve(name.concat("-latin.midi")));
      groove(song, Ensembles.funk(80), dir.resolve(name.concat("-funk.midi")));
    }
  }

  private void groove(Song song, Ensemble ensemble, Path path) {
    Part part = ensemble.play(song, 4); // ctx.repeatCount
    MidiOut mf = new HumanizingMidiOut(new MidiFile());
    part.perform(mf);
    mf.save(path);
  }
  
}
