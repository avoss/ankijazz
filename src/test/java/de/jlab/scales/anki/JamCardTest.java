package de.jlab.scales.anki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import de.jlab.scales.jtg.RenderContext;
import de.jlab.scales.midi.song.Ensembles;
import de.jlab.scales.midi.song.MidiTestUtils;
import de.jlab.scales.theory.Note;

public class JamCardTest {

  FretboardPosition position = FretboardPosition.HIGH;
  
  @Test
  public void testSongId() {
    String id1 = card().getAssetId();
    String id2 = card().getAssetId();
    assertEquals(id1, id2);
  }
  
  @Test
  public void testJson() {
    Map<String, Object> json = card().getJson();
    assertEquals(8, json.size());
    assertEquals("Funk", json.get("style"));
  }
  
  @Test
  public void testCsv() {
    assertThat(card().getCsv()).contains("[sound:AnkiJazz");
    assertThat(card().getCsv()).doesNotContain(position.getLabel());
    assertThat(guitarCard().getCsv()).contains(position.getLabel());
  }
  
  private JamCard guitarCard() {
    return new JamCard(Note.C, RenderContext.ANKI, MidiTestUtils.createStaticSongWrapper(), () -> Ensembles.funk(100), position);
  }
  
  private JamCard card() {
    return new JamCard(Note.C, RenderContext.ANKI, MidiTestUtils.createStaticSongWrapper(), () -> Ensembles.funk(100));
  }

}
