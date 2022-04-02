package com.ankijazz.theory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * approximate stack of thirds.
 */
public class Stacker {
  private Set<Note> set;
  private List<Note> list;
  private Note root;
  
  public Stacker (Scale scale) {
    this.set = scale.asSet();
    this.root  = scale.getRoot();
    this.list = new ArrayList<>();
    
    move(root);
    
    if (!move(root.major3()) && !move(root.minor3())) {
      move(root.four());
    }

    if (!move(root.five()) && !move(root.flat5())) {
      move(root.sharp5());
    }
    
    if (!move(root.minor7()) && !move(root.major7())) {
      move(root.major6());
    }
    
    move(root.minor7());
    move(root.major7());
    move(root.flat9());
    move(root.nine());
    move(root.sharp9());
    move(root.four());
    move(root.flat5());
    move(root.sharp5());
    move(root.major6());
  }
  
  private boolean move(Note note) {
    if (!set.contains(note)) {
      return false;
    }
    set.remove(note);
    list.add(note);
    return true;
  }
  
  public List<Note> getStackedThirds() {
    return list;
  }
}