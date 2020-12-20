package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.MidiTestUtils.event;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.jlab.scales.midi.song.EventParser.PatternMetadata;

public class EventParserTest {

  static class TestEventProcessor implements EventProcessor {
    
    private List<Event> events = new ArrayList<>();
    private StringBuilder pattern = new StringBuilder();
    
    @Override
    public void event(Event event) {
      events.add(event);
      pattern.append("x");
    }

    @Override
    public void empty() {
      pattern.append(".");
    }
    
    public List<Event> getEvents() {
      return events;
    }
    public String getPattern() {
      return pattern.toString();
    }
    
  }
  
  @Test
  public void testEventParser() {
    assertParse("x...",  event(4, 0, 127, 1));
    assertParse(".5..",  event(4, 1, 63, 1));
    assertParse(".5.. | x",  event(5, 1, 63, 1), event(5, 4, 127, 1));
    assertParse("x-..",  event(4, 0, 127, 2));
    assertParse("x--.",  event(4, 0, 127, 3));
    assertParse("x---",  event(4, 0, 127, 4));
  }

  private void assertParse(String pattern, Event ... expected) {
    TestEventProcessor processor = new TestEventProcessor();
    PatternMetadata metadata = EventParser.parseEvents(processor, pattern, MidiTestUtils.PATTERN_ID);
    assertThat(metadata.getEvents().size()).isEqualTo(expected.length);
    assertThat(metadata.getPatternLength()).isEqualTo(expected[0].getPatternLength());
    assertThat(processor.getEvents()).containsExactly(expected);
    String expectedProcessorPattern = pattern.replaceAll("[ |]", "").replace("-", ".").replace("5", "x");
    assertThat(processor.getPattern()).isEqualTo(expectedProcessorPattern);
  }

}
