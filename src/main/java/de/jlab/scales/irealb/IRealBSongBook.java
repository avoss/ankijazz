package de.jlab.scales.irealb;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class IRealBSongBook {
  
  List<IRealBSong> songs = Lists.newArrayList();
  
  @Override
  public String toString() {
    return "irealbook://" + Joiner.on("\n").join(songs);
  }

}
