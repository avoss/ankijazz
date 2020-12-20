package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.MidiTestUtils;

public class JamCardTest {

  @Test
  public void testSongId() {
    String id1 = new JamCard(RenderContext.ANKI, MidiTestUtils.createStaticSong(), Ensembles.funk(100)).assetId();
    String id2 = new JamCard(RenderContext.ANKI, MidiTestUtils.createStaticSong(), Ensembles.funk(100)).assetId();
    assertEquals(id1, id2);
  }

}
