package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Test;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.SongFactory.Feature;

public class SongFactoryTest {

  @Test
  public void testKeys() {
    assertNumberOfSongs(EnumSet.of(Test, EachKey), 13);
    assertNumberOfSongs(EnumSet.of(Test, AllKeys), 4);
  }

  private void assertNumberOfSongs(EnumSet<Feature> features, int expectedNumberOfSongs) {
    SongFactory factory = factory(features);
    List<SongWrapper> songs = factory.generate(16);
    assertEquals(expectedNumberOfSongs, songs.size());
  }

  private SongFactory factory(EnumSet<Feature> features) {
    LoopIteratorFactory iterators = Utils.fixedLoopIteratorFactory();
    return new SongFactory(iterators, new ProgressionFactory(iterators), features);
  }

  @Test
  public void assertNoDuplicateSongsAreGenerated() {
    SongFactory factory = factory(EnumSet.of(Test, Workouts, AllKeys, EachKey));
    RenderContext context = RenderContext.ANKI;
    List<SongWrapper> list = factory.generate(context.getNumberOfBars());
    Set<Song> set = list.stream().map(SongWrapper::getSong).collect(toSet());
    assertEquals(list.size(), set.size());
  }

  @Test
  public void testProgressionMustNotSpanSongBoundaries() {
    assertEquals(12, factory(EnumSet.of(Test, AllKeys)).progressionMustNotSpanSongBoundaries(12, 16));
  }
}
