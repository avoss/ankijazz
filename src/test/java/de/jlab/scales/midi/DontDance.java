package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.seq;
import static de.jlab.scales.midi.Parts.timeSignature;

import org.junit.Test;

import de.jlab.scales.midi.wav.WavFile;

public class DontDance extends WAV {

  @Test
  public void main() {
    Part bar44 = seq(timeSignature(4, 4), hi, r, r, r, seq(lo, r, r, r).repeat(3));
    Part bar38 = seq(timeSignature(3, 8), hi, r, lo, r, lo, r);

    Part countIn = bar44.repeat(4);
  
    // siehe excel
    Part intro = seq(bar44.repeat(8 + 8 + 7 + 6 + 2 + 9));
    Part melody = seq(bar44.repeat(5), bar38, seq(bar44.repeat(4), bar38).repeat(3));
    Part theme1 = seq(melody, bar44.repeat(3));
    Part theme2 = seq(bar44.repeat(5), bar38, bar44.repeat(6 + 8 + 9));
    Part improSax = seq(bar44.repeat(4 * 8));
    Part bridge = seq(bar44.repeat(6 + 8));
    Part ending = seq(melody, theme1);
    
    Part song = seq(r, countIn, intro, theme1, theme2, theme1, improSax, bridge, ending);
    
    MidiOut mf = new WavFile();
    mf.setTempo(160);
    song.perform(mf);
    mf.save("lame/DontDance.wav");
    
  }

}
