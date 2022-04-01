package de.jlab.scales.sheet;

@lombok.Data @lombok.Builder(toBuilder = true)
public class RenderContext {
  public static final RenderContext FULL = RenderContext.builder()
      .imageWidth(2000)
      .imageHeight(2828)
      .layoutColumns(8)
      .layoutRows(16)
      .numberOfBars(96)
      .withHeader(true)
      .withBarNumbers(true)
      .repeat(1)
      .build();

  public static final RenderContext ANKI = RenderContext.builder()
      .imageWidth(1000)
      .imageHeight(600)
      .layoutColumns(4)
      .layoutRows(5)
      .numberOfBars(16)
      .withHeader(false)
      .withBarNumbers(false)
      .repeat(1)
      .build();
  
  private int imageWidth;
  private int imageHeight;
  private int layoutRows;
  private int layoutColumns;
  private int numberOfBars;
  private boolean withHeader;
  private boolean withBarNumbers;
  private int repeat;
  
  
  public RenderContext scaleWidth(int newImageWidth) {
    double factor = (double) newImageWidth / imageWidth;
    return toBuilder()
      .imageWidth((int) (imageWidth * factor))
      .imageHeight((int) (imageHeight * factor))
      .build();
  }
}
