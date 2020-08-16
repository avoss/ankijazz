package de.jlab.scales.theory;

@lombok.Data
@lombok.RequiredArgsConstructor(staticName = "of")
public class KeySignature {
  private final Note root;
  private final Accidental accidental;
  
}
