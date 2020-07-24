package de.jlab.scales.theory;
import static de.jlab.scales.theory.Note.A;
import static de.jlab.scales.theory.Note.Ab;
import static de.jlab.scales.theory.Note.B;
import static de.jlab.scales.theory.Note.Bb;
import static de.jlab.scales.theory.Note.C;
import static de.jlab.scales.theory.Note.D;
import static de.jlab.scales.theory.Note.Db;
import static de.jlab.scales.theory.Note.E;
import static de.jlab.scales.theory.Note.Eb;
import static de.jlab.scales.theory.Note.F;
import static de.jlab.scales.theory.Note.G;
import static de.jlab.scales.theory.Note.Gb;


public enum ScaleType {
  
  Major("Major Scale", new Scale(C, D, E, F, G, A, B), "Ionian", "Dorian", "Phrygian", "Lydian", "Mixolydian", "Aeolean", "Locrian"),
  MelodicMinor("Melodic Minor Scale", new Scale(C, D, Eb, F, G, A, B), "Dorian #7", "Phrygian #6", "Lydian #5", "Mixolydian #4", "Aeolean #3", "Altered", "Ionian #1"),
  HarmonicMinor("Harmonic Minor Scale", new Scale(C, D, Eb, F, G, Ab, B), "Aeolean #7", "Locrian #6", "Ionian #5", "Dorian #4", "Phrygian Dominant", "Lydian #2", "Mixolydian #1"),
  HarmonicMajor("Harmonic Major Scale", new Scale(C, D, E, F, G, Ab, B), "Ionian b6", "Dorian b5", "Phrygian b4", "Lydian b3", "Mixolydian b2", "Aeolean b1", "Locrian b7"),
  Diminished("Diminished Half/Whole Scale", new Scale(C, Db, Eb, E, Gb, G, A, Bb)),
  WholeTone("Whole Tone Scale", new Scale(C, D, E, Gb, Ab, Bb)),
  Augmented("Augmented Scale", new Scale(C, Eb, E, G, Ab, B)),
  Blues("Blues Scale", new Scale(C, Eb, F, Gb, G, Bb)),
  Minor7Pentatonic("Minor Pentatonic Scale", new Scale(C, Eb, F, G, Bb)),
  Minor6Pentatonic("Minor 6 Pentatonic Scale", new Scale(C, Eb, F, G, A)),
  //Minor6Flat5Pentatonic("m6b5 Pent", new Scale(C, Eb, F, Gb, A)),
  //Minor6Sharp5Pentatonic("m6#5 Pent", new Scale(C, Eb, F, Ab, A)),
  //Minor7Flat5Pentatonic("m7b5 Pent", new Scale(C, Eb, F, Gb, Bb)),
  RagaPentatonic("Raga",  new Scale(C, E, F, G, Bb)),
  // why triads? what about arpeggios?
  MajorTriad("Major Triad", new Scale(C, E, G)),
  MinorTriad("Minor Triad", new Scale(C, Eb, G)),
  DiminishedTriad("Diminished Triad", new Scale(C, Eb, Gb)),
  AugmentedTriad("Augmented Triad", new Scale(C, E, Ab)),
  Sus4Triad("Sus4 Triad", new Scale(C, F, G)),
  ;

  public static final ScaleType[] ALL = {Major, MelodicMinor, HarmonicMinor, HarmonicMajor, Diminished, WholeTone, Augmented, Blues, 
    Minor7Pentatonic, Minor6Pentatonic};
  public static final ScaleType[] TRIADS = {MajorTriad, MinorTriad, DiminishedTriad, AugmentedTriad, Sus4Triad};
  
  private final String name;
  private final Scale scale;

  ScaleType(String name, Scale scale, String ... modeNames) {
    this.name = name;
    this.scale = scale;
    
  }

  public final String getName() {
    return name;
  }

  public final Scale getScale() {
    return scale;
  }
  
}
