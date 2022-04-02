package de.jlab.scales.midi.song;

public class PatternParser {
  private PatternParser() {}
  
  public interface Handler {
    void onValue(double value);
    void onDash();
    void onEmpty();
    void onNext();
  }
  
 
  public static void parse(String pattern, Handler handler) {
    for (char c : pattern.toCharArray()) {
      switch(c) {
      case'0':
      case'1':
      case'2':
      case'3':
      case'4':
      case'5':
      case'6':
      case'7':
      case'8':
      case'9':
        handler.onValue((double)(c - '0')/10.0);
        break;
      case'x':
        handler.onValue(1.0);
        break;
      case'.':
        handler.onEmpty();
        break;
      case'-':
        handler.onDash();
        break;
      case'>':
        handler.onNext();
        break;
      default:
        // ignore everything else
        break;
      }
    }
  }

}
