package com.ankijazz.fretboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Preview {
  private Preview() {
  }

  public static void preview(final Image image) {

    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Preview");
    frame.setLayout(new FlowLayout());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    ImageIcon imageIcon = new ImageIcon(image);
    JLabel jLabel = new JLabel();
    jLabel.setIcon(imageIcon);
    frame.getContentPane().add(jLabel, BorderLayout.CENTER);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    waitUntilClosed(frame);

  }

  private static void waitUntilClosed(JFrame frame) {
    while (frame.isVisible()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
  }

}
