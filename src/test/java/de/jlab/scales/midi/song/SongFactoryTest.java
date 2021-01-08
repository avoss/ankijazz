package de.jlab.scales.midi.song;

import static de.jlab.scales.TestUtils.assertFileContentMatches;
import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.SomeKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.Test;
import static de.jlab.scales.midi.song.SongFactory.Feature.Workouts;
import static java.util.stream.Collectors.toList;
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
import de.jlab.scales.midi.song.SongFactory.KeyFactory;
import static org.assertj.core.api.Assertions.*;

public class SongFactoryTest {
  
  @Test
  public void testKeyFactoryMinorMajor() {
    SongFactory factory = factory(Set.of());
    KeyFactory[] eachKey = factory.eachKey().toArray(new KeyFactory[0]);
    eachKey[0].nextSong(false);
    assertEquals("Key of C Major", eachKey[0].getTitle());
    eachKey[0].nextSong(false);
    assertEquals("Key of Db Major", eachKey[0].getTitle());
    eachKey[1].nextSong(true);
    assertEquals("Key of B Minor", eachKey[1].getTitle());
  }

  @Test
  public void testKeys() {
    assertNumberOfSongs(Set.of(Test, EachKey), 3 * 13);
    assertNumberOfSongs(Set.of(Test, AllKeys), 3 * 4);
  }

  private void assertNumberOfSongs(Set<Feature> features, int expectedNumberOfSongs) {
    SongFactory factory = factory(features);
    List<SongWrapper> songs = factory.generate(16);
    assertEquals(expectedNumberOfSongs, songs.size());
  }

  private SongFactory factory(Set<Feature> features) {
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
  
  @Test
  public void testAllKeySignatures() {
    assertThat(factory(Set.of()).allKeySignatures().size()).isEqualTo(13);
  }
  
  @Test
  public void testSongGenerator() {
    assertFactoryGenerates(factory(Set.of(Test, AllKeys)), "testAllKeys.txt");
    assertFactoryGenerates(factory(Set.of(Test, EachKey)), "testEachKey.txt");
    assertFactoryGenerates(factory(Set.of(Test, SomeKeys)), "testSomeKeys.txt");
  }

  private void assertFactoryGenerates(SongFactory factory, String fileName) {
    List<SongWrapper> wrapper = factory.generate(16);
    List<String> actual = wrapper.stream().map(SongWrapper::toString).collect(toList());
    assertFileContentMatches(actual, SongFactoryTest.class, fileName);
  }
}
