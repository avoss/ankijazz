package de.jlab.scales.jtg;


abstract class Layout {

  private final double width;
  private final double height;
  private final int columns;
  private final int rows;
  private final double headerLines;
  private final double margin;
  private final double lineHeight;
  protected RenderContext context;

  Layout(RenderContext context) {
    this.context = context;
    this.width = context.getImageWidth();
    this.height = context.getImageHeight();
    this.rows = context.getLayoutRows();
    this.columns = context.getLayoutColumns();
    this.headerLines = (context.isWithHeader() ? 1 : 0);
    this.margin = width / columns / 2;
    this.lineHeight = height / (headerLines + rows);
  }

  float x(int column) {
    return (float)(margin + column * (width - 2 * margin) / columns);
  }

  float y(int row) {
    return (float)((row + headerLines) * lineHeight);
  }

  int row(int chordIndex) {
    return chordIndex / columns;
  }

  int column(int chordIndex) {
    return chordIndex % columns;
  }

  double width() {
    return width;
  }

  double height() {
    return height;
  }

  double lineHeight() {
    return lineHeight;
  }

  double chordHeight() {
    return lineHeight / 2d;
  }

  double chordWidth() {
    return (width - 2 * margin) / columns;
  }

  int rows() {
    return rows;
  }

  int columns() {
    return columns;
  }

  public double margin() {
    return margin;
  }
}
