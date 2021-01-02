package de.jlab.scales.midi.song;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.midi.song.ProgressionParser.ChordFactory;
import de.jlab.scales.theory.KeySignature;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 * 
 * maj triads: "A D G C"
 * min triads: "Am Dm G C"
 * dim triads: "Ao Do G C"
 * aug triads: "Am Do G+ Cm"
 * all triads: "Am Do G+ C"
 * 
 * blues: "A7 D7 A7 A7 D7 D7 A7 A7 E7 D7 A7 E7" KeyOf A! or D?
 * jazzblues: "A7 D9 A7 [A7sus4 A7#5] D9 D#dim7 A7 Gb7#5 E7#5#9 [A9 F#7#5] [Bm9 (E7#5#9, E7#5b9)]"
 * 
 * focus on m7, 7, maj7: "Am7 Dm7 G7 Cmaj7"
 * focus on m7b5, 7b9  : "F#mb5 Bmb5 E7b9 Am7"
 * focus on m6         : "F#m6 Bm6 E7b9 Am7"
 * focus on m6         : "F#m6 Bm6 E7b9 Am7"
 */
public class ProgressionFactory {

  private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  private ObjectReader reader = mapper.readerFor(ProgressionSet.class);

  public static class Progression {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String comment;
    @Getter
    @Setter
    private String progression;
    private List<ChordFactory> chordFactories = null;
    private LoopIteratorFactory loopIteratorFactory;

    private List<ChordFactory> getChordFactories() {
      if (chordFactories == null) {
        chordFactories = new ProgressionParser(loopIteratorFactory).parse(progression);
      }
      return chordFactories;
    }

    public List<Bar> create(KeySignature keySignature) {
      return getChordFactories().stream().map(factory -> factory.create(keySignature)).map(chords -> new Bar(chords)).collect(toList());
    }

    public int getNumberOfBars() {
      return getChordFactories().size();
    }

    public void setLoopIteratorFactory(LoopIteratorFactory loopIteratorFactory) {
      this.loopIteratorFactory = loopIteratorFactory;
    }
  }

  @Getter
  @Setter
  public static class ProgressionSet {
    String id;
    List<Progression> progressions;

    public void setLoopIteratorFactory(LoopIteratorFactory loopIteratorFactory) {
      for (Progression progression : progressions) {
        progression.setLoopIteratorFactory(loopIteratorFactory);
      }
    }
  }

  @Getter
  private List<ProgressionSet> progressionSets = new ArrayList<>();
  private LoopIteratorFactory loopIteratorFactory;

  public ProgressionFactory(LoopIteratorFactory loopIteratorFactory) {
    this.loopIteratorFactory = loopIteratorFactory;
    progressionSets.add(load("test.yaml"));
    progressionSets.add(load("triads.yaml"));
    progressionSets.add(load("workouts.yaml"));
  }

  ProgressionSet load(String yamlResourceName) {
    try {
      ProgressionSet factories = reader.<ProgressionSet>readValue(ProgressionFactory.class.getResource(yamlResourceName));
      factories.setLoopIteratorFactory(loopIteratorFactory);
      return factories;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
