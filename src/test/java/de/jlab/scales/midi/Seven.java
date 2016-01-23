package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.seq;
import static de.jlab.scales.midi.Parts.timeSignature;

import org.junit.Test;

import de.jlab.scales.midi.wav.WavFile;

public class Seven extends WAV {

  @Test
  public void main() {
    Part bar78 = seq(timeSignature(7, 8), hi, r, r, r, seq(lo, r, r, r).repeat(2), seq(lo, r));
    Part bar68 = seq(timeSignature(6, 8), hi, r, r, r, r, r);

    Part countIn = bar78.repeat(4);
  
    Part zeppelin = seq(bar78.repeat(8));
    Part melodyUp = seq(bar78.repeat(5));
    Part melodyChord78 = seq(bar78.repeat(2));
    Part melodyDn = seq(bar78.repeat(2));
    Part melodyChord68 = seq(bar68.repeat(4));
    
    Part melody1 = seq(zeppelin, melodyUp, melodyChord78, melodyDn);
    Part melody2 = seq(zeppelin, melodyUp, melodyChord68);
    Part remaining = seq(bar78.repeat(200));
    
    Part song = seq(r, countIn, melody1, melody2, bar68.repeat(8 * 8), remaining);
    
    MidiOut mf = new WavFile();
    mf.setTempo(105);
    song.perform(mf);
    mf.save("lame/Seven.wav");
    
  }

}
