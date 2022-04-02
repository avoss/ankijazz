package com.ankijazz.midi.song;

import static com.ankijazz.midi.song.MidiTestUtils.event;
import static com.ankijazz.theory.Scales.Cm7;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.Test;

import com.ankijazz.midi.AbstractPart;
import com.ankijazz.midi.CompositePart;
import com.ankijazz.midi.MidiOut;
import com.ankijazz.midi.Part;
import com.ankijazz.midi.Parts;
import com.ankijazz.midi.song.Bar;
import com.ankijazz.midi.song.BarProcessor;
import com.ankijazz.midi.song.BarProcessorFactory;
import com.ankijazz.midi.song.Chord;
import com.ankijazz.midi.song.Event;
import com.ankijazz.midi.song.Song;
import com.ankijazz.theory.Scale;

import lombok.EqualsAndHashCode;

public class BarProcessorTest {

  @EqualsAndHashCode(callSuper = true)
  static class TestPart extends AbstractPart {
    Scale scale;
    
    TestPart(Scale scale) {
      this.scale = scale;
    }
    
    @Override
    public void perform(MidiOut midiOut) {
    }
    
  }
  
  class TestPlayer implements BiFunction<Event, Scale, Part>  {
    List<Scale> scales = new ArrayList<>();
    @Override
    public Part apply(Event event, Scale scale) {
      scales.add(scale);
      return new TestPart(scale);
    }
  }
  
  @Test
  public void testEmpty() {
    CompositePart mock = mock(CompositePart.class);
    BarProcessorFactory bpf = new BarProcessorFactory(16, new TestPlayer());
    bpf.empty();
    bpf.empty();
    bpf.empty();
    BarProcessor processor = bpf.create();
    processor.accept(mock, Bar.of(cm7()));
    verify(mock, times(3)).add(Parts.rest(1, 16));
  }


  @Test
  public void testEvent() {
    CompositePart mock = mock(CompositePart.class);
    BarProcessorFactory bpf = new BarProcessorFactory(16, new TestPlayer());
    bpf.event(event(0, 80, 3));
    bpf.empty();
    BarProcessor processor = bpf.create();
    Bar bar = Bar.of(cm7());
    Song.of(bar);
    processor.accept(mock, bar);
    verify(mock, times(2)).add(Parts.rest(1, 16));
    verify(mock, times(1)).add(new TestPart(Cm7));
  }

  @Test
  public void testRightChordIsChosen() {
    CompositePart mock = mock(CompositePart.class);
    BarProcessorFactory bpf = new BarProcessorFactory(16, new TestPlayer());
    bpf.event(event(2, 80, 3));
    bpf.event(event(8, 80, 3));
    BarProcessor processor = bpf.create();
    Scale dm7 = Cm7.transpose(2);
    Bar bar = Bar.of(cm7(), dm7());
    Song.of(bar);
    processor.accept(mock, bar);
    verify(mock, times(2)).add(Parts.rest(1, 16));
    verify(mock, times(1)).add(new TestPart(Cm7));
    verify(mock, times(1)).add(new TestPart(dm7));
  }

  private Chord cm7() {
    return Chord.of(Cm7, "Cm7");
  }
  
  private Chord dm7() {
    return Chord.of(Cm7.transpose(2), "Dm7");
  }
}
