package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.SongFactory.Feature.AllKeys;
import static de.jlab.scales.midi.song.SongFactory.Feature.EachKey;
import static de.jlab.scales.midi.song.SongFactory.Feature.Test;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

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
    List<Song> songs = factory.generate(16);
    assertEquals(expectedNumberOfSongs, songs.size());
  }

  private SongFactory factory(EnumSet<Feature> features) {
    return new SongFactory(new ProgressionFactory(), features);
  }

  @Test
  public void assertNoDuplicateSongsAreGenerated() {
    SongFactory factory = factory(EnumSet.of(Test, AllKeys, EachKey));
    RenderContext context = RenderContext.ANKI;
    List<Song> list = factory.generate(context.getNumberOfBars()).stream().collect(toList());
    Set<Song> set = factory.generate(context.getNumberOfBars()).stream().collect(toSet());
    assertEquals(list.size(), set.size());
  }
  
}
