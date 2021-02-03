package de.jlab.scales.jtg;

import static de.jlab.scales.Utils.linux;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import de.jlab.scales.jtg.IdSong.IdBar;
import de.jlab.scales.midi.song.Song;

public class PngImageRenderer extends Layout {
  private final String copyright = "(C) 2020 www.AnkiJazz.com - all rights reserved";

  private final int barLineSpacing;

  private final IdSong song;
  private Graphics2D g;
  private Font chordFont = chordFont();
  private Font subFont = subFont();
  private Font superFont = superFont();
  private Font barNumberFont = barNumberFont();

  private RenderContext originalContext;

  public PngImageRenderer(RenderContext context, Song song) {
    super(context.scaleWidth(1000 * context.getLayoutColumns() / 8)); // used to be 8 columns in initial version ??????????????
    this.originalContext = context;
    this.song = new IdSong(song);
    this.barLineSpacing = context.isWithBarNumbers() ? 20 : 10;
  }
  
  private Font chordFont() {
    return linux() ? new Font("Lucida Sans", Font.BOLD, 27) : new Font("Comic Sans MS", Font.BOLD, 27);
  }
  
  private Font subFont() {
    return linux() ? new Font("Lucida Sans", Font.PLAIN, 15) : new Font("Comic Sans MS", Font.PLAIN, 15);
  }

  private Font superFont() {
    return linux() ? new Font("Lucida Sans", Font.PLAIN, 18) : new Font("Comic Sans MS", Font.PLAIN, 18);
  }
  
  private Font barNumberFont() {
    return linux() ? new Font("Lucida Sans", Font.BOLD, 9) : new Font("Comic Sans MS", Font.PLAIN, 9);
  }

  public void renderSheet(BufferedImage image) {

    g = (Graphics2D) image.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    mapUserSpaceToDeviceSpace(image);
    clearBackground();
    drawHeader();
    drawCopyright();
    drawBars();
  }

  private void mapUserSpaceToDeviceSpace(BufferedImage image) {
    // keep aspect ratio
    double factor = (double) image.getWidth() / width();
    g.setTransform(AffineTransform.getScaleInstance(factor, factor));
  }

  private void clearBackground() {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, (int)width(), (int)height());
  }

  private void drawHeader() {
    if (!context.isWithHeader()) {
      return;
    }
    String title = "Some Title"; // song.getTitle();
    g.setFont(chordFont);
    g.setColor(Color.BLACK);
    int length = g.getFontMetrics().stringWidth(title);
    float x = (float)((width() - length) / 2);
    float y = (float)lineHeight();
    g.drawString(title, x, y);
  }
  
  private void drawCopyright() {
    g.setFont(subFont);
    g.setColor(Color.LIGHT_GRAY);

    int length = g.getFontMetrics().stringWidth(copyright);
    float x = (float)(width() - length)/2;
    float y = (float)(height() - g.getFontMetrics().getHeight());
    g.drawString(copyright, x, y);
  }

  private void drawBars() {
    song.getBars().forEach(this::drawBar);
  }


  private void drawBar(IdBar bar) {
    drawBarLineLeft(bar);
    drawChords(bar);
    if (isLineBreak(bar) || isLastBar(bar)) {
      drawBarLineRight(bar);
    }
  }

  private boolean isLastBar(IdBar bar) {
    return song.indexOf(bar) == song.getBars().size() - 1;
  }

  private boolean isLineBreak(IdBar bar) {
    return column(song.indexOf(bar)) == (columns() - 1);
  }

  private void drawBarLineLeft(IdBar bar) {
    int barIndex = song.indexOf(bar);
    int row = row(barIndex);
    int column = column(barIndex);
    drawBarLine(row, column);
    if (context.isWithBarNumbers()) {
      drawBarNumber(bar, row, column);
    }
  }

  private void drawBarNumber(IdBar bar, int row, int column) {
    if (column > 0) {
      return;
    }
    String barNumber = Integer.toString(song.indexOf(bar) + 1);
    g.setFont(barNumberFont);
    g.drawString(barNumber, x(column) + 10, topOfBarLine(row) + 10 );
  }

  private void drawBarLineRight(IdBar bar) {
    int barIndex = song.indexOf(bar);
    drawBarLine(row(barIndex), column(barIndex) + 1);
  }

  private void drawBarLine(int row, int column) {
    int topOfBarLine = topOfBarLine(row);
    g.setColor(Color.BLACK);
    g.fillRect((int)x(column), topOfBarLine, 2, (int) chordHeight());
  }

  private int topOfBarLine(int row) {
    return (int) (y(row) + chordHeight() * 1.25);
  }

  class MyRenderer implements ChordRenderer.Renderer {
    private float x;
    private float y;
    private Font chordFont;
    private Font superFont;
    private Font subFont;
    private float xOffset;

    public void setPosition(float x, float y) {
      this.x = x + xOffset;
      this.y = y;
    }
    
    MyRenderer(float xOffset, AffineTransform transform) {
      this.xOffset = xOffset;
      this.chordFont = PngImageRenderer.this.chordFont.deriveFont(transform);
      this.superFont = PngImageRenderer.this.superFont.deriveFont(transform);
      this.subFont = PngImageRenderer.this.subFont.deriveFont(transform);
    }

    @Override
    public void renderNormal(String s) {
      g.setFont(chordFont);
      g.drawString(s, x, y);
      x += g.getFontMetrics().stringWidth(s);
    }

    @Override
    public void renderSuper(String s) {
      g.setFont(chordFont);
      int rootHeight = g.getFontMetrics().getAscent();
      g.setFont(superFont);
      int accHeight = g.getFontMetrics().getAscent();
      g.drawString(s, x, y - (rootHeight - accHeight) - accHeight / 3);
    }

    @Override
    public void renderSub(String s) {
      g.setFont(subFont);
      int accHeight = g.getFontMetrics().getAscent();
      g.drawString(s, x, y + accHeight / 6);
    }

  }
  
  MyRenderer singleChordRenderer = new MyRenderer(0, AffineTransform.getScaleInstance(1, 1));
  MyRenderer leftChordRenderer = new MyRenderer(0, AffineTransform.getScaleInstance(0.7, 1));
  MyRenderer rightChordRenderer = new MyRenderer((x(1)-x(0))/2, translateLeft(AffineTransform.getScaleInstance(0.6, 1)));
  
  private void drawChords(IdBar bar) {
    g.setColor(Color.BLACK);
    int index = song.indexOf(bar);
    float x = x(column(index)) + barLineSpacing;
    float y = y(row(index) + 1);
    if (bar.getChords().size() == 1) {
      String symbol = bar.getChords().get(0).getChord().getSymbol();
      singleChordRenderer.setPosition(x, y);
      new ChordRenderer(singleChordRenderer).parse(symbol);
    } else if (bar.getChords().size() == 2) {
      String leftSymbol = bar.getChords().get(0).getChord().getSymbol();
      leftChordRenderer.setPosition(x, y);
      new ChordRenderer(leftChordRenderer).parse(leftSymbol);

      String rightSymbol = bar.getChords().get(1).getChord().getSymbol();
      rightChordRenderer.setPosition(x, y);
      new ChordRenderer(rightChordRenderer).parse(rightSymbol);
      
    }


  }

  private AffineTransform translateLeft(AffineTransform transform) {
    transform.translate(-5, 0);
    return transform;
  }

  public void renderTo(Path path) {
    try {
      BufferedImage image = render();
      File out = path.toFile();
      Files.createDirectories(path.getParent());
      ImageIO.write(image,  "png", out);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public BufferedImage render() {
    BufferedImage image = new BufferedImage(originalContext.getImageWidth(), originalContext.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
    renderSheet(image);
    return image;
  }

}