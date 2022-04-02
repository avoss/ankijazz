package com.ankijazz.theory;

import static com.ankijazz.theory.BuiltinScaleType.DiminishedHalfWhole;
import static com.ankijazz.theory.BuiltinScaleType.HarmonicMajor;
import static com.ankijazz.theory.BuiltinScaleType.HarmonicMinor;
import static com.ankijazz.theory.BuiltinScaleType.Major;
import static com.ankijazz.theory.BuiltinScaleType.MelodicMinor;
import static com.ankijazz.theory.BuiltinScaleType.WholeTone;

import java.util.Comparator;
import java.util.List;

public final class ScalesDifficultyComparator implements Comparator<ScaleInfo> {
  private final List<ScaleType> types = List.of(Major, MelodicMinor, HarmonicMinor, WholeTone, DiminishedHalfWhole, HarmonicMajor);

  @Override
  public int compare(ScaleInfo a, ScaleInfo b) {
    if (a.isInversion() != b.isInversion()) {
      return a.isInversion() ? 1 : -1;
    }
    if (a.getScaleType() != b.getScaleType()) {
      return Integer.compare(indexOf(a), indexOf(b));
    }
    return Integer.compare(a.getKeySignature().getNumberOfAccidentals(), b.getKeySignature().getNumberOfAccidentals()); 
  }

  private int indexOf(ScaleInfo info) {
    int index = types.indexOf(info.getScaleType());
    return index >= 0 ? index : 1000;
  }
  
  public List<ScaleType> getTypes() {
    return types;
  }
}