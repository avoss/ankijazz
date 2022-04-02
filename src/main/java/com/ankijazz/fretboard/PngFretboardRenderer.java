package de.jlab.scales.fretboard;

import static de.jlab.scales.Utils.linux;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import de.jlab.scales.Utils;
import de.jlab.scales.Utils.Interpolator;

public class PngFretboardRenderer implements FretboardRenderer<BufferedImage> {
//  private final String copyright = "(C) 2021 www.AnkiJazz.com - all rights reserved";
  private final String copyright = "(C) 2021 www.AnkiJazz.com - Anki shared deck license";
  
  private static final double IMAGE_SIZE = 0.7;

  private final Fretboard fretboard;
  private final int minFret;
  private final int maxFret;
  private boolean renderFretNumber;
  
  private final int stringDistance = 100;
  private final int fretDistance = stringDistance * 3;
  private final int marginX = fretDistance / 2;
  private final int marginY = stringDistance;
  private final int height;
  private final int width;
  
  private Graphics2D g;


  private Font copyrightFont() {
    return linux() ? new Font("Lucida Sans", Font.PLAIN, 35) : new Font("Comic Sans MS", Font.PLAIN, 35);
  }

  private Font fretNumberFont() {
    return linux() ? new Font("Lucida Sans", Font.BOLD, 50) : new Font("Comic Sans MS", Font.BOLD, 50);
  }
  
  public PngFretboardRenderer(Fretboard fretboard, boolean renderFretNumber) {
    this(fretboard, fretboard.getMinFret(), fretboard.getMaxFret(), renderFretNumber);
  }
  
  public PngFretboardRenderer(Fretboard fretboard, int minFret, int maxFret, boolean renderFretNumber) {
    this.fretboard = fretboard;
    this.minFret = minFret;
    this.maxFret = maxFret;
    this.renderFretNumber = renderFretNumber;
    this.height = stringDistance * (fretboard.getStrings().size() + 1);
    this.width = fretDistance * (maxFret - minFret + 1);
  }
  
  @Override
  public BufferedImage render() {
    
    BufferedImage image = new BufferedImage((int)(width * IMAGE_SIZE), (int)(height * IMAGE_SIZE), BufferedImage.TYPE_3BYTE_BGR);
    g = (Graphics2D) image.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    mapUserSpaceToDeviceSpace(image);
    clearBackground();
    drawCopyright();
    drawEmptyFretboard();
    drawFrets();
    drawStrings();
    drawMarkers();
    drawFretNumber();
    
    return image;
  }

  private void drawMarkers() {
    for (GuitarString string : fretboard.getStrings()) {
      drawMarkers(string);
    }
  }

  class PngMarkerRenderer implements MarkerRenderer {
    int markerSize = (stringY(0) - stringY(1)) * 4 / 5;

    @Override
    public void renderEmpty(GuitarString string, int fret) {
      // ignore
    }

    @Override
    public void renderForeground(GuitarString string, int fret) {
      renderMarker(Color.DARK_GRAY, string, fret);
    }

    @Override
    public void renderBackground(GuitarString string, int fret) {
      renderMarker(Color.LIGHT_GRAY, string, fret);
    }

    @Override
    public void renderRoot(GuitarString string, int fret) {
      renderMarker(Color.RED, string, fret);
    }

    private void renderMarker(Color color, GuitarString string, int fret) {
      g.setColor(color);
      g.fillOval(markerX(fret), markerY(string.getStringIndex()), markerWidth(), markerHeight());
    }

    private int markerHeight() {
      return markerSize;
    }

    private int markerWidth() {
      return markerSize;
    }

    private int markerY(int stringIndex) {
      return stringY(stringIndex) - markerSize/2;
    }

    private int markerX(int fret) {
      return (fretX(fret) + fretX(fret + 1)) / 2 - markerSize/2;
    }

  }
  
  private void drawMarkers(GuitarString string) {
    MarkerRenderer renderer = new PngMarkerRenderer();
    for (int fret = minFret; fret <= maxFret; fret++) {
      Marker marker = string.markerOf(fret);
      marker.render(renderer, string, fret);
    }
  }

  private void drawStrings() {
    g.setColor(Color.DARK_GRAY);
    Interpolator thickness = Utils.interpolator(0, numberOfStrings(), 15, 4);
    for (int string = 0; string < numberOfStrings(); string++) {
      g.setStroke(new BasicStroke(thickness.apply(string), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      g.drawLine(fretboardX(), stringY(string), fretboardX() + fretboardWidth(), stringY(string));
    }
  }

  private int numberOfStrings() {
    return fretboard.getStrings().size();
  }

  private int stringY(int string) {
    int margin = fretboardHeight() * 2 / (numberOfStrings() * 3);
    return Utils.interpolator(0, numberOfStrings() -1, fretboardY() + fretboardHeight() - margin, fretboardY() + margin).apply(string);
  }

  private void drawFrets() {
    g.setColor(Color.GRAY);
    g.setStroke(new BasicStroke(6, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    for (int fret = minFret + 1; fret <= maxFret; fret++) {
      g.drawLine(fretX(fret), fretboardY(), fretX(fret), fretboardY() + fretboardHeight());
    }
  }

  private int fretX(int fret) {
    return Utils.interpolator(minFret, maxFret + 1, fretboardX(), fretboardX() + fretboardWidth()).apply(fret);
  }

  private void drawEmptyFretboard() {
    g.setColor(Color.GRAY);
    g.setStroke(new BasicStroke(2));
    g.drawRect(fretboardX(), fretboardY(), fretboardWidth(), fretboardHeight());
  }

  private int fretboardX() {
    return marginX;
  }

  private int fretboardY() {
    return marginY;
  }

  private int fretboardWidth() {
    return width - 2 * marginX;
  }
  
  private int fretboardHeight() {
    return height - 2 * marginY;
  }

  private void mapUserSpaceToDeviceSpace(BufferedImage image) {
    double sx = (double) image.getWidth() / width;
    double sy = (double) image.getHeight() / height;
    g.setTransform(AffineTransform.getScaleInstance(sx, sy));
  }

  private void clearBackground() {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);
  }
  
  private void drawCopyright() {
    g.setFont(copyrightFont());
    g.setColor(Color.LIGHT_GRAY);

    int length = g.getFontMetrics().stringWidth(copyright);
    float x = (float)(width - length)/2;
    float y = (float)(height - g.getFontMetrics().getHeight());
    g.drawString(copyright, x, y);
  }
  
  private void drawFretNumber() {
    if (!renderFretNumber) {
      return;
    }
    String fretNumber = Integer.toString(fretboard.getMinFret());
    g.setFont(fretNumberFont());
    g.setColor(Color.GRAY);
    float fretX = (fretX(minFret) + fretX(minFret+1))/2;
    float stringWidth = g.getFontMetrics().stringWidth(fretNumber);
    float x = fretX - stringWidth/2;
    g.drawString(fretNumber, x, g.getFontMetrics().getHeight());
  }
  
}
