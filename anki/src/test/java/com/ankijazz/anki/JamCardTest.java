package com.ankijazz.anki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.ankijazz.midi.song.Ensembles;
import com.ankijazz.midi.song.MidiTestUtils;
import com.ankijazz.sheet.RenderContext;
import com.ankijazz.theory.Note;

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
    assertEquals(9, json.size());
    assertEquals("Funk", json.get("style"));
  }
  
  @Test
  public void testCsv() {
    assertThat(card().getCsv()).contains("[sound:AnkiJazz");
    assertThat(card().getCsv()).doesNotContain(position.getLabel());
    assertThat(guitarCard().getCsv()).contains(position.getLabel());
  }
  
  private JamCard guitarCard() {
    return JamCard.builder()
      .instrument(Note.C)
      .context(RenderContext.ANKI)
      .wrapper(MidiTestUtils.createStaticSongWrapper())
      .ensembleSupplier(() -> Ensembles.funk(100))
      .position(position)
      .build();
  }
  
  private JamCard card() {
    return JamCard.builder()
        .instrument(Note.C)
        .context(RenderContext.ANKI)
        .wrapper(MidiTestUtils.createStaticSongWrapper())
        .ensembleSupplier(() -> Ensembles.funk(100))
        .build();
  }

}
