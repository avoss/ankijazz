package de.jlab.scales.irealb;

public class IRealB {
// https://github.com/ironss/accompaniser/blob/master/irealb_parser.lua
  public String unobfusc(String s) {
    String r = "";
    while (s.length() > 50) {
      String p = s.substring(0, 50);
      s = s.substring(50);
      if (s.length() < 2) {
        r = r + p;
      }
      else {
        r = r + obfusc50(p);
      }
      
    }
    return r + s;
  }

  private String obfusc50(String s) {
    char[] array = s.toCharArray();
    for (int i = 0; i < 5; i++)
      swap(array, i, 49-i);
    for (int i = 10; i < 24; i++)
      swap(array, i, 49-i);
    return new String(array);
  }

  private void swap(char[] array, int i, int j) {
    char tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }
  
  

}
