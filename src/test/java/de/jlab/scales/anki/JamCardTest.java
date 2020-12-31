package de.jlab.scales.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.MidiTestUtils;

public class JamCardTest {

  @Test
  public void testSongId() {
    String id1 = new JamCard(RenderContext.ANKI, MidiTestUtils.createStaticSongWrapper(), Ensembles.funk(100)).getAssetId();
    String id2 = new JamCard(RenderContext.ANKI, MidiTestUtils.createStaticSongWrapper(), Ensembles.funk(100)).getAssetId();
    assertEquals(id1, id2);
  }

}
