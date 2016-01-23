package de.jlab.scales.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WriteImageType {
  static public void main(String args[]) throws Exception {

    // VirtualDub creates a movie from this ...
    for (int i = 0; i < 100; i++) {
      System.out.println(i);
      drawImage(i);
    }
    
      

  }

  private static void drawImage(int i) throws IOException {
    int width = 600, height = 400;

    // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
    // into integer pixels
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D ig2 = bi.createGraphics();

    Font font = new Font("TimesRoman", Font.BOLD, 20);
    ig2.setFont(font);
    String message = "www.java2s.com!";
    FontMetrics fontMetrics = ig2.getFontMetrics();
    int stringWidth = fontMetrics.stringWidth(message);
    int stringHeight = fontMetrics.getAscent();
    ig2.setPaint(Color.red);
    //ig2.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);
    ig2.drawString(message, i , height / 2 + stringHeight / 4);

    String fileName = String.format("c:\\temp\\image%04d.png", i);
    ImageIO.write(bi, "PNG", new File(fileName));
  }
}