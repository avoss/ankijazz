package de.jlab.scales.anki;

import static de.jlab.scales.theory.Accidental.FLAT;
import static de.jlab.scales.theory.Accidental.SHARP;
import static de.jlab.scales.theory.ScaleUniverse.MODES;
import static de.jlab.scales.theory.Scales.CMajor;
import static de.jlab.scales.theory.Scales.allKeys;
import static de.jlab.scales.theory.Scales.commonModes;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.jlab.scales.Utils;
import de.jlab.scales.difficulty.DifficultyModel;
import de.jlab.scales.difficulty.DifficultyModel.DoubleTerm;
import de.jlab.scales.theory.IntervalAnalyzer;
import de.jlab.scales.theory.KeySignature;
import de.jlab.scales.theory.Note;
import de.jlab.scales.theory.Scale;
import de.jlab.scales.theory.ScaleInfo;
import de.jlab.scales.theory.ScaleUniverse;
import de.jlab.scales.theory.Scales;

public class ModesTheoryDeck extends AbstractDeck<SimpleCard> {

  private static final String FRONT = "front";
  private static final String BACK = "back";
  private static final String TASK = "task";
  private static final String PARENT_NAME = "parentName";
  private static final String PARENT_TYPE = "parentType";
  private static final String PARENT_ROOT = "parentRoot";
  private static final String MODE_NAME = "modeName";
  private static final String MODE_TYPE = "modeType";
  private static final String MODE_ROOT = "modeRoot";

  protected ModesTheoryDeck() {
    super("Modes Theory");
    
    for (Scale scale : allKeys(commonModes(false))) {
      for (ScaleInfo info : MODES.infos(scale)) {
        double difficulty = computeScaleDifficulty(info);
        spellScales(difficulty, info);
        nameParent(difficulty, info);
        nameMode(difficulty, info);
      }
    }
    enharmonics();
    modeIntervals();
    spellChords();
  }

  private SimpleCard card(double difficulty, String task, String front, String back) {
    SimpleCard card = new SimpleCard(difficulty, FRONT, BACK, TASK, PARENT_NAME, PARENT_TYPE, PARENT_ROOT, MODE_NAME, MODE_TYPE, MODE_ROOT);
    card.put(FRONT, front);
    card.put(BACK, back);
    card.put(TASK, task);
    return card;
  }

  private SimpleCard card(double difficulty, String task, String front, String back, ScaleInfo modeInfo) {
    SimpleCard card = card(difficulty, task, front, back);
    ScaleInfo parentInfo = modeInfo.getParentInfo();
    card.put(PARENT_NAME, parentInfo.getScaleName());
    card.put(PARENT_TYPE, parentInfo.getTypeName());
    card.put(PARENT_ROOT, parentInfo.getKeySignature().notate(parentInfo.getScale().getRoot()));
    card.put(MODE_NAME, modeInfo.getScaleName());
    card.put(MODE_TYPE, modeInfo.getTypeName());
    card.put(MODE_ROOT, modeInfo.getKeySignature().notate(modeInfo.getScale().getRoot()));
    return card;
  }
  
  private void enharmonics() {
    final String question = "<div>What is the <b>enharmonic</b> equivalent of <b>%s</b>?</div>"; 
    for (Note note : Note.values()) {
      if (!CMajor.contains(note)) {
        add(card(0, "Enharmonics", format(question, note.getName(SHARP)), divb(note.getName(FLAT))));
        add(card(0, "Enharmonics", format(question, note.getName(FLAT)), divb(note.getName(SHARP))));
      }
    }
  }

  void modeIntervals() {
    IntervalAnalyzer analyzer = new IntervalAnalyzer();
    for (Scale scale : commonModes()) {
      ScaleInfo modeInfo = MODES.findFirstOrElseThrow(scale);
      String front = format("<div>What are the <b>intervals</b> of <b>%s</b>?</div>", modeInfo.getTypeName());
      String back = divb(analyzer.analyze(scale).toString());
      SimpleCard card = card(computeScaleDifficulty(modeInfo), "ModeIntervals", front, back);
      card.put(MODE_TYPE, modeInfo.getTypeName());
      card.put(PARENT_TYPE, modeInfo.getParentInfo().getTypeName());
      add(card);
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
    add(card(difficulty, "NameMode", front, back, modeInfo));
  }

  private void nameParent(double difficulty, ScaleInfo modeInfo) {
    if (!modeInfo.isInversion()) {
      return;
    }
    ScaleInfo parentInfo = modeInfo.getParentInfo();
    String front = format("<div>What is the <b>parent</b> scale of <b>%s</b>?</div>", modeInfo.getScaleName());
    String back = divb(parentInfo.getScaleName());
    add(card(difficulty, "NameParent", front, back, modeInfo));
  }

  private void spellScales(double difficulty, ScaleInfo modeInfo) {
    String front = format("<div>What are the <b>notes</b> of <b>%s</b>?</div>", modeInfo.getScaleName());
    String back = divb(modeInfo.getKeySignature().toString(modeInfo.getScale()));
    add(card(difficulty, "SpellScale", front, back, modeInfo));
  }

  Collection<? extends Scale> chordsToSpell() {
    List<Scale> chords = new ArrayList<>();
    Iterator<Note> roots = Utils.loopIterator(Arrays.asList(Note.values()));
    for (Scale chord : Scales.allChords()) {
      for (int i = 0; i < 7; i++) {
        chords.add(chord.transpose(roots.next()));
      }
    }
    return chords;
  }
  
  private void spellChords() {
    DifficultyModel model = new DifficultyModel();
    DoubleTerm numberOfAccidentalsDifficulty = model.doubleTerm(0, 6, 100);
    DoubleTerm numberOfNotesDifficulty = model.doubleTerm(3, 5, 50);
    model.doubleTerm(30).update(1);
    for (Scale chord : chordsToSpell()) {
      for (ScaleInfo chordInfo : ScaleUniverse.CHORDS.infos(chord)) {
        numberOfAccidentalsDifficulty.update(chordInfo.getKeySignature().getNumberOfAccidentals());
        numberOfNotesDifficulty.update(chord.getNumberOfNotes());
        String chordLabel = chord.getNumberOfNotes() == 3 ? "Triad" : "Chord";
        String front = format("<div>What are the <b>notes</b> of <b>%s</b> %s%s?</div>", chordInfo.getScaleName(), chordLabel, chordSignature(chordInfo));
        String back = divb(chordInfo.getKeySignature().toString(chordInfo.getScale().stackedThirds()));
        SimpleCard card = card(model.getDifficulty(), "SpellChord", front, back);
        card.put(MODE_NAME, chordInfo.getScaleName());
        card.put(MODE_TYPE, chordInfo.getTypeName());
        card.put(MODE_ROOT, chordInfo.getKeySignature().notate(chordInfo.getScale().getRoot()));
        add(card);
      }
    }
  }

  private String chordSignature(ScaleInfo info) {
    Note root = info.getScale().getRoot();
    if (!Scales.CMajor.contains(root)) {
      return "";
    }
    KeySignature keySignature = info.getKeySignature();
    if (keySignature.getNumberOfAccidentals() == 0) {
      return "";
    }
    if (info.getScaleType().getKeySignatures(root).size() == 1) {
      return "";
    }
    return format(" (using <b>%s</b>)", keySignature.getAccidental() == FLAT ? "flats" : "sharps");
  }

  private String divb(String x) {
    return format("<div><b>%s</b></div>", x);
  }

  private double computeScaleDifficulty(ScaleInfo info) {
    return new ScaleModel(info).getDifficulty();
  }
  
}
