package de.jlab.scales.midi;

import static de.jlab.scales.midi.Parts.note;
import static de.jlab.scales.midi.Parts.par;
import static de.jlab.scales.midi.Parts.rest;
import static de.jlab.scales.midi.Parts.seq;

import org.junit.Test;

public class Clave {
  Part r = rest(16);
  Part h = seq(note(9, 42, 80, 24), r);
  Part c = seq(note(9, 56, 127, 24), r);

  @Test
  public void sonClave() {
    MidiFile mf = new MidiFile();
    mf.setTempo(100);
    
    Part hihat = seq(h, r, r, r).repeat(4);
    Part cowbell = seq(c, r, r, c,   r, r, c, r,   r, r, c, r,   c, r, r, r);

    Part clave = par(hihat, cowbell);
    clave.repeat(100).perform(mf);
    mf.save("temp/SonClave.mid");
  }

  @Test
  public void rumbaClave() {
    MidiFile mf = new MidiFile();
    mf.setTempo(100);
    
    Part hihat = seq(h, r, r, r).repeat(4);
    Part cowbell = seq(c, r, r, c,   r, r, r, c,   r, r, c, r,   c, r, r, r);

    Part clave = par(hihat, cowbell);
    clave.repeat(100).perform(mf);
    mf.save("temp/RumbaClave.mid");
  }
}
