package de.jlab.scales.midi.song;

import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.CHORD;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.END_BAR;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.END_CHOICE;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.EOF;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.START_BAR;
import static de.jlab.scales.midi.song.ProgressionParser.Scanner.Token.START_CHOICE;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.jlab.scales.Utils.LoopIteratorFactory;
import de.jlab.scales.theory.ChordParser;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Scale;
/*
 * 
 * maj triads: "A D G C"
 * min triads: "Am Dm G C"
 * dim triads: "Ao Do G C"
 * aug triads: "Am Do G+ Cm"
 * all triads: "Am Do G+ (Cm C)"
 * 
 * blues: "A7 D7 A7 A7 D7 D7 A7 A7 E7 D7 A7 E7" KeyOf A! or D?
 * jazzblues: "A7 D9 A7 [A7sus4 A7#5] D9 D#dim7 A7 Gb7#5 E7#5#9 [A9 F#7#5] [Bm9 (E7#5#9, E7#5b9)]"
 * 
 * focus on m7, 7, maj7: "Am7 Dm7 G7 Cmaj7"
 * focus on m7b5, 7b9  : "F#mb5 Bmb5 E7b9 Am7"
 * focus on m6         : "F#m6 Bm6 E7b9 Am7"
 * focus on m6         : "F#m6 Bm6 E7b9 Am7"
 * 
 */
public class ProgressionParser {
  
  private Scanner scanner;
  private LoopIteratorFactory loopIteratorFactory;

  static class Scanner {
    public enum  Token { BOF, START_CHOICE, END_CHOICE, START_BAR, END_BAR, CHORD, EOF }

    private static final Pattern PATTERN = Pattern.compile("\\s*(\\()|(\\))|(\\[)|(\\])|([^\\s\\(\\)\\[\\]]+)", Pattern.CASE_INSENSITIVE);
    private Matcher matcher;
    private Token token = Token.BOF;
    private String value;

    public Scanner(String string) {
      matcher = PATTERN.matcher(string);
    }

    public Token next() {
      if (token == EOF) {
        return token;
      }
      if (!matcher.find()) {
        token = EOF;
        return token;
      }
      value = matcher.group(0);
      if (matcher.group(1) != null) {
        token = START_CHOICE;
      } else if (matcher.group(2) != null) {
        token = END_CHOICE;
      } else if (matcher.group(3) != null) {
        token = START_BAR;
      } else if (matcher.group(4) != null) {
        token = END_BAR;
      } else if (matcher.group(5) != null) {
        token = CHORD;
      } else {
        throw new IllegalStateException();
      }
      return token;
    }

    public Token getToken() {
      return token;
    }
    
    public String getValue() {
      return value;
    }

  }

  public ProgressionParser(LoopIteratorFactory loopIteratorFactory) {
    this.loopIteratorFactory = loopIteratorFactory;
  }

  public List<ChordFactory> parse(String progression) {
    scanner = new Scanner(progression);
    return recurse();
  }

  private List<ChordFactory> recurse() {
    List<ChordFactory> list = new ArrayList<>();
    while (true) {
      switch (scanner.next()) {
      case CHORD:
        list.add(new SimpleChordFactory(scanner.getValue()));
        break;
      case END_BAR:
      case END_CHOICE:
      case EOF:
        return list;
      case START_BAR:
        list.add(sequence());
        break;
      case START_CHOICE:
        list.add(choice());
        break;
      default: 
        throw new IllegalStateException("Unknown Token: " + scanner.getToken());
      }
    }
  }

  private ChordFactory choice() {
    return new ChoiceChordFactory(loopIteratorFactory, recurse());
  }

  private ChordFactory sequence() {
    return new SequenceChordFactory(recurse());
  }

  static interface ChordFactory {
    List<Chord> create(KeySignature keySignature);
  }

  static class ChoiceChordFactory implements ChordFactory {
    private Iterator<ChordFactory> iterator;
    public ChoiceChordFactory(LoopIteratorFactory loopIteratorFactory, List<ChordFactory> factories) {
      iterator = loopIteratorFactory.iterator(factories);
    }
    @Override
    public List<Chord> create(KeySignature keySignature) {
      return iterator.next().create(keySignature);
    }
  }
  
  static class SequenceChordFactory implements ChordFactory {
    private List<ChordFactory> factories;
    public SequenceChordFactory(List<ChordFactory> factories) {
      this.factories = factories;
    }
    @Override
    public List<Chord> create(KeySignature keySignature) {
      return factories.stream().flatMap(f -> f.create(keySignature).stream()).collect(toList());
    }
  }
  
  static class SimpleChordFactory implements ChordFactory {

    private Scale scale;
    private String type;
    
    SimpleChordFactory(String string) {
      scale = ChordParser.parseChord(string);
      type = string.replaceAll("[A-G][b#]?", "");
    }
    
    @Override
    public List<Chord> create(KeySignature keySignature) {
      Scale transposed = scale.transpose(keySignature.getNotationKey().ordinal());
      String symbol = keySignature.notate(transposed.getRoot()).concat(type);
      return List.of(Chord.of(transposed, symbol));
    }
    
  }
}
