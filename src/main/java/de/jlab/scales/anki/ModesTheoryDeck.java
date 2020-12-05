package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static java.lang.String.format;

import de.jlab.scales.Utils;
import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.difficulty.DifficultyModel.DoubleTerm;
import de.jlab.scales.theory.IntervalAnalyzer;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

/**
 * Ankie fields??
 * <ul>
 * <li>front</li>
 * <li>back</li>
 * <li>task, e.g. SpellScale, SpellChord, NameMode, NameParent, ...</li>
 * <li>modeName, e.g. D-Dorian or Dm7</li>
 * <li>modeType, e.g. Dorian or m7</li>
 * <li>modeRoot, e.g. D</li>
 * <li>parentName, e.g. C-Major or notation-root for chords?</li>
 * <li>parentType, e.g. Major or major for chords even if chord is subset of melodic minor???</li>
 * <li>parentRoot, e.g. C</li>
 * <li>accidental: flat or sharp or none (for key of C)?
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 *
 */
public class ModesTheoryDeck extends AbstractDeck {

  protected ModesTheoryDeck() {
    super("Modes Theory");
    
    for (Scale scale : allKeys(commonModes(false))) {
      for (ScaleInfo info : MODES.infos(scale)) {
        double difficulty = computeDifficulty(info);
        spellScales(difficulty, info);
        nameParent(difficulty, info);
        nameMode(difficulty, info);
      }
    }
    modeIntervals();
    spellChords();
  }


  private void enharmonics() {
    final String pattern = "<div>What is the <b>enharmonic</b> equivalent of <b>%s</b>?</div>"; 
    final String tags = Utils.tags("Task=Enharmonics");
    for (Note note : Note.values()) {
      if (!CMajor.contains(note)) {
        add(0, format(pattern, note.getName(SHARP)), note.getName(FLAT), tags);
        add(0, format(pattern, note.getName(FLAT)), note.getName(SHARP), tags);
      }
    }
  }

  void modeIntervals() {
    IntervalAnalyzer analyzer = new IntervalAnalyzer();
    for (Scale scale : commonModes()) {
      ScaleInfo info = MODES.info(scale);
      String front = format("<div>What are the <b>intervals</b> of <b>%s</b>?</div>", info.getTypeName());
      String back = divb(analyzer.analyze(scale).toString());
      add(computeDifficulty(info), front, back, Utils.tags("Task=ModeIntervals", "Mode=" + info.getTypeName()));
    }
  }

  private void nameMode(double difficulty, ScaleInfo modeInfo) {
    if (!modeInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = modeInfo.getParentInfo();
    int modeIndex = modeInfo.getModeIndex() + 1;
    String front = format("<div>What is <b>mode %d</b> of <b>%s</b>?</div>", modeIndex, parentInfo.getScaleName());
    String back = divb(modeInfo.getScaleName());
    String tags = scaleTags("NameMode", parentInfo);
    add(difficulty, front, back, tags);
  }

  private void nameParent(double difficulty, ScaleInfo scaleInfo) {
    if (!scaleInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = scaleInfo.getParentInfo();
    String front = format("<div>What is the <b>parent</b> scale of <b>%s</b>?</div>", scaleInfo.getScaleName());
    String back = divb(parentInfo.getScaleName());
    String tags = scaleTags("NameParent", scaleInfo);
    add(difficulty, front, back, tags);
  }

  private void spellScales(double difficulty, ScaleInfo info) {
    String front = format("<div>What are the <b>notes</b> of <b>%s</b>?</div>", info.getScaleName());
    String back = divb(info.getKeySignature().toString(info.getScale()));
    String tags = scaleTags("SpellScale", info);
    add(difficulty, front, back, tags);
  }

  private void spellChords() {
    DifficultyModel model = new DifficultyModel();
    DoubleTerm numberOfAccidentalsDifficulty = model.doubleTerm(0, 6, 100);
    DoubleTerm numberOfNotesDifficulty = model.doubleTerm(3, 5, 50);
    model.doubleTerm(30).update(1);
    for (Scale chord : allKeys(Scales.allChords())) {
      for (ScaleInfo info : ScaleUniverse.CHORDS.infos(chord)) {
        numberOfAccidentalsDifficulty.update(info.getKeySignature().getNumberOfAccidentals());
        numberOfNotesDifficulty.update(chord.getNumberOfNotes());
        String front = format("<div>What are the <b>notes</b> of <b>%s</b> %s?</div>", info.getScaleName(), chord.getNumberOfNotes() == 3 ? "Triad" : "Chord");
        String back = divb(info.getKeySignature().toString(info.getScale()));
        String tags = chordTags("SpellChord", info);
        add(model.getDifficulty(), front, back, tags);
      }
    }
  }

  private String scaleTags(String task, ScaleInfo info) {
    return tags("Mode", task, info); 
  }

  private String chordTags(String task, ScaleInfo info) {
    return tags("Chord", task, info); 
  }

  private String tags(String scaleType, String task, ScaleInfo info) {
    return Utils.tags(
        format("Task=%s", task),
        format("%s=%s", scaleType, info.getTypeName()), 
        format("Root=%s", info.getKeySignature().notate(info.getScale().getRoot())));
  }
  
  private String divb(String x) {
    String back = format("<div><b>%s</b></div>", x);
    return back;
  }
 
  private double computeDifficulty(ScaleInfo info) {
    ScaleModel model = new ScaleModel(info);
    return model.getDifficulty();
  }
  
}
