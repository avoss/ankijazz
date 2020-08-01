package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.seq;
import static de.jlab.scales.midi.Parts.timeSignature;

import org.junit.Test;

import de.jlab.scales.midi.wav.WavFile;

public class Click extends WAV {

  @Test
  public void main() {
    Part bar44 = seq(timeSignature(4, 4), hi, r, r, r, seq(lo, r, r, r).repeat(3));


    for (int bpm = 60; bpm <= 120; bpm += 30) {
      int tenMinutes = 10 * bpm / 4;
      Part song = seq(r, bar44.repeat(tenMinutes));
      System.out.println(bpm);
      MidiOut mf = new WavFile();
      mf.setTempo(bpm);
      song.perform(mf);
      mf.save(String.format("lame/Click%03d.wav", bpm));
    }

  }

}
