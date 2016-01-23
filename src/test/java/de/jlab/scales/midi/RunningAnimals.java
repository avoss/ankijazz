package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.par;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

import org.junit.Test;

public class RunningAnimals extends Base16 {
  Part x = seq(seq(HI, r, r).repeat(6), seq(HI, HI, r, HI, r, HI, HI, r, HI, r, r, r, HI, r, r,r, HI, r, r, r, r, r, HI, r.repeat(7)));
  Part brk = par(x, seq(q, q, Q, q, q, q, Q, q, q, q));
  
  @Test
  public void animals() {
    MidiFile mf = new MidiFile();
    mf.setTempo(127);

    Part countin = seq(rest(1), bar.repeat(2));
    Part intro = seq(cb, bar.repeat(4));
    Part chrom = seq(cb, bar.repeat(4));
    Part theme = seq(cb, bar.repeat(12));
    Part quak  = seq(cb, bar.repeat(16));
    Part stop  = seq(cb, bar, cb, Q, q);
    Part song = seq(countin, intro, chrom, theme, chrom, theme, quak, stop, brk, bar.repeat(200));
    //Part song = seq(bar.repeat(2), brk).repeat(100);

    song.perform(mf);
    mf.save("temp/animals.mid");
  }
  
}
