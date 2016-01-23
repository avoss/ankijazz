package de.jlab.scales.midi;

public class Lyrics extends AbstractPart {
  
  private final String text;

  public Lyrics(String text) {
    this.text = text;
  }

  @Override
  public void perform(MidiOut midiOut) {
    midiOut.addLyrics(text);
  }

}
