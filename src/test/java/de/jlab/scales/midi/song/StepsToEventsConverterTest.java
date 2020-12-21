package de.jlab.scales.midi.song;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class StepsToEventsConverterTest {

  @Test
  public void test() {
//    StepsToEventsConverter converter = new StepsToEventsConverter(4, 16);
//    List<String> eventIds = converter.bar(PatternParser.parse("--.. 8--- ..8. > ..8-"));
//    converter.bar(PatternParser.parse("---- > ..8- -.8- > -.8-"));
    // ">" takes chord from next beat (possibly from next bar)
    // start with "---" will extend last note of last bar
    // use IdSong to index chords? might be good idea once we add repetition etc ...
    // IdSong.getChordByBeatIndex() - kümmert sich um die Anzahl Akkorde in einem Takt
    // getPlayer() bekommt keinen Bar sondern einen Chord.
  }

}
