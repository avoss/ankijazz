package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static java.lang.String.format;

import de.jlab.scales.Utils;
import de.jlab.scales.theory.IntervalAnalyzer;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;

public class ModesTheoryDeck extends AbstractDeck {

  protected ModesTheoryDeck() {
    super("Modes Theory");
    
    for (Scale scale : allKeys(commonModes(false))) {
      for (ScaleInfo info : MODES.infos(scale)) {
        ScaleModel model = new ScaleModel(info, false);
        int difficulty = model.getDifficulty();
        spellNotes(difficulty, info);
        nameParent(difficulty, info);
        nameMode(difficulty, info);
      }
    }
    addModeIntervals();
  }

  private void addEnharmonics() {
    final String pattern = "<div>What is the enharmonic equivalent of <b>%s</b>?</div>"; 
    final String tags = Utils.tags("Enharmonics");
    for (Note note : Note.values()) {
      if (!CMajor.contains(note)) {
        add(0, format(pattern, note.getName(SHARP)), note.getName(FLAT), tags);
        add(0, format(pattern, note.getName(FLAT)), note.getName(SHARP), tags);
      }
    }
  }

  void addModeIntervals() {
    IntervalAnalyzer analyzer = new IntervalAnalyzer();
    for (Scale scale : commonModes()) {
      ScaleInfo info = MODES.info(scale);
      String front = format("<div>What are the intervals of <b>%s</b>?</div>", info.getTypeName());
      String back = analyzer.analyze(scale).toString();
      add(0, front, back, Utils.tags("ModeIntervals", info.getTypeName()));
    }
  }


  private void nameMode(int difficulty, ScaleInfo modeInfo) {
    if (!modeInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = modeInfo.getParentInfo();
    int modeIndex = modeInfo.getModeIndex() + 1;
    String front = format("<div>What is mode <b>%d</b> of <b>%s</b>?</div>", modeIndex, parentInfo.getScaleName());
    String back = format("<div><b>%s</b></div>", modeInfo.getScaleName());
    String tags = tags("NameMode", parentInfo);
    add(difficulty, front, back, tags);
  }

  private void nameParent(int difficulty, ScaleInfo scaleInfo) {
    if (!scaleInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = scaleInfo.getParentInfo();
    String front = format("<div>What is the parent scale of <b>%s</b>?</div>", scaleInfo.getScaleName());
    String back = format("<div><b>%s</b></div>", parentInfo.getScaleName());
    String tags = tags("NameParent", scaleInfo);
    add(difficulty, front, back, tags);
  }

  private void spellNotes(int difficulty, ScaleInfo info) {
    String front = format("<div>What are the notes of <b>%s</b>?</div>", info.getScaleName());
    String back = format("<div><b>%s</b></div>", info.getKeySignature().toString(info.getScale()));
    String tags = tags("SpellNotes", info);
    add(difficulty, front, back, tags);
  }

  private String tags(String task, ScaleInfo info) {
    return Utils.tags(task, info.getTypeName(), info.getScale().getRoot().getName(FLAT));
  }
  
  
  
}
