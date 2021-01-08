package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.ProgressionFactory.ChordProgressionSet.loadChordProgression;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.midi.song.ProgressionParser.ChordFactory;
import de.jlab.scales.theory.KeySignature;
import lombok.Getter;
import lombok.Setter;

public class ProgressionFactory {

  public interface Progression {
    String getTitle();
    String getComment();
    String getProgression();
    int getNumberOfBars();
    List<Bar> create(KeySignature keySignature);
    boolean isMinor();
  }
  
  public static class ChordProgression implements Progression {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String comment;
    @Getter
    @Setter
    private String progression;
    @Getter
    @Setter
    private boolean minor;
    
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
  
  public interface ProgressionSet {
    String getId();
    String getTitle();
    List<? extends Progression> getProgressions();
  }

  public static class ChordProgressionSet implements ProgressionSet {
    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static ObjectReader reader = mapper.readerFor(ChordProgressionSet.class);
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private List<ChordProgression> progressions;

    public void setLoopIteratorFactory(LoopIteratorFactory loopIteratorFactory) {
      for (ChordProgression progression : progressions) {
        progression.setLoopIteratorFactory(loopIteratorFactory);
      }
    }
    
    public static ProgressionSet loadChordProgression(String yamlResourceName, LoopIteratorFactory loopIteratorFactory) {
      try {
        ChordProgressionSet factories = reader.<ChordProgressionSet>readValue(ProgressionFactory.class.getResource(yamlResourceName));
        factories.setLoopIteratorFactory(loopIteratorFactory);
        return factories;
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  @Getter
  private List<ProgressionSet> progressionSets = new ArrayList<>();

  public ProgressionFactory(LoopIteratorFactory loopIteratorFactory) {
    progressionSets.add(loadChordProgression("Test.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("Triads.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("Workouts.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("TwoFiveOnes.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("ExtTwoFiveOnes.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("SimpleBlues.yaml", loopIteratorFactory));
    progressionSets.add(loadChordProgression("JazzBlues.yaml", loopIteratorFactory));
  }

}
