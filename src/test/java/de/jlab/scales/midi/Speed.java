package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.seq;

import org.junit.Test;

public class Speed extends Base16 {
  
  final int step = 3;
  final int minBpm = 90;
  final int maxRepeat = 4;
  final int maxBpm = 120;
  final int endBpm = 90;

  @Test
  public void test() {
    MidiFile mf = new MidiFile();
    for (int bpm = minBpm; bpm < maxBpm; bpm += step) {
      mf.setTempo(bpm);
      bar.perform(mf);
    }
    mf.setTempo(maxBpm);
    for (int i = 0; i < maxRepeat; i++)
      seq(cb, bar).perform(mf);
    for (int bpm = maxBpm - step; bpm > endBpm; bpm -= step) {
      mf.setTempo(bpm);
      bar.perform(mf);
    }
    mf.save("temp/speed.mid");
  }

}
