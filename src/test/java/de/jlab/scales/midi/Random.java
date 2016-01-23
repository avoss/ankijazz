package de.jlab.scales.midi;

import static de.jlab.scales.midi.Drum.Cowbell;
import static de.jlab.scales.midi.Drum.MetronomeBell;
import static de.jlab.scales.midi.Drum.MetronomeClick;
import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.par;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

import org.junit.Test;

/**
 * generates the click track for our song "Random"
 * 
 * @author andreas
 *
 */
public class Random {

  Part r = rest(24);
  Part cb = note(Cowbell, 127, 32);
  Part hi = seq(note(MetronomeBell, 127, 32), r);
  Part lo = seq(note(MetronomeClick, 127, 32), r);
  Part HI = seq(cb, r);
  
  Part bar = seq(hi, r, r, r, r, r, seq(lo, r, r, r, r, r).repeat(3));
  Part a = seq(r, HI, r);
  Part b = seq(HI, r, HI);
  Part c = seq(r, r, r);
  Part d = seq(r, r, HI);
  Part e = seq(HI, r, r);
  Part brk = par(seq(a, b, c, a, b, d, d, e), bar);


  @Test
  public void random() {
    MidiFile mf = new MidiFile();
    mf.setTempo(78);

    Part countin = seq(rest(1), bar.repeat(2));
    Part intro1 = par(cb, bar.repeat(3));
    Part intro2 = par(cb, bar.repeat(4));
    Part theme1 = par(cb, bar.repeat(10));
    Part theme2 = par(cb, bar.repeat(8));
    Part bridge = par(cb, bar.repeat(8));
    Part solo = seq(theme1, theme2, brk.repeat(2), theme1, theme2, brk.repeat(2), bar.repeat(2), brk.repeat(2));
    Part tail = bar.repeat(128);
    Part song = seq(countin, intro1, intro2, theme1, theme2, bridge, solo, tail);
    //Part song = sequence(bar.repeat(2), brk.repeat(2)).repeat(8);

    song.perform(mf);
    mf.save("temp/random.mid");
  }

}
