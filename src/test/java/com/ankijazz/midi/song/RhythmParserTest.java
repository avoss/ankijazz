package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.MidiTestUtils.event;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RhythmParserTest {

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
  public void testSinglePattern() {
    assertParse("x...",  event(0, 127, 1));
    assertParse(".5..",  event(1, 63, 1));
    assertParse(".5.. | x",  event(1, 63, 1), event(4, 127, 1));
    assertParse("x-..",  event(0, 127, 2));
    assertParse("x--.",  event(0, 127, 3));
    assertParse("x---",  event(0, 127, 4));
    assertParse("> x---",  event(0, 127, 4, 1));
  }

  private void assertParse(String pattern, Event ... expected) {
    RhythmParser parser = new RhythmParser(4);
    TestEventProcessor processor = new TestEventProcessor();
    List<String> eventIds = parser.parse(pattern, processor);
    
    assertThat(eventIds.size()).isEqualTo(expected.length);
    assertThat(processor.getEvents()).containsExactly(expected);
    String expectedProcessorPattern = pattern.replaceAll("[ >|]", "").replace("-", ".").replace("5", "x");
    assertThat(processor.getPattern()).isEqualTo(expectedProcessorPattern);
  }
  
  @Test
  public void testMultiplePatterns() {
    RhythmParser parser = new RhythmParser(4);
    TestEventProcessor processor = new TestEventProcessor();
    parser.parse("--.. > x...", processor);
    assertThat(processor.getEvents()).containsExactly(event(4, 127, 1, 2));

    processor = new TestEventProcessor();
    parser.parse(".... > ...x", processor);
    assertThat(processor.getEvents()).containsExactly(event(1, 7, 127, 1, 2));
    parser.close();
    parser.close();
    assertThat(processor.getEvents()).containsExactly(event(1, 7, 127, 3, 2));
    
    
  }

}
