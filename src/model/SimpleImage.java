package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a rectangular image with at least 1 pixel, which can produce modified versions of
 *   itself, and return its dimensions and color of pixel at a given location.
 */
public class SimpleImage implements Image {

  /**
   * Invariant: pixels will always have dimensions [width][height].
   * Invariant: pixels has at least 1 pixel.
   * Invariant: pixels is not jagged.
   */
  private final Pixel[][] pixels;
  private final int width;
  private final int height;

  /**
   * Constructs the image from an array of pixels.
   * @param pixels the rectangular 2-d array of pixels which will comprise this image.
   * @throws IllegalArgumentException if pixels was null, or non-rectangular,
   *                                  or contained any null pixels.
   */
  public SimpleImage(Pixel[][] pixels) throws IllegalArgumentException {
    if (pixels == null) {
      throw new IllegalArgumentException("Given array of pixels was null.");
    }

    if (pixels.length == 0) {
      throw new IllegalArgumentException("Given array of pixels had a dimension 0.");
    }

    if (pixels[0].length == 0) {
      throw new IllegalArgumentException("Given array of pixels had a dimension 0.");
    }

    for (int i = 0; i < pixels.length; i++) {
      if (pixels[i] == null || pixels[0].length != pixels[i].length) {
        throw new IllegalArgumentException("Pixel array was jagged.");
      }
      for (int j = 0; j < pixels[i].length; j++) {
        if (pixels[i][j] == null) {
          throw new IllegalArgumentException("Null pixel present at " + i + "," + j + ".");
        }
      }
    }

    this.pixels = pixels;
    this.height = pixels[0].length;
    this.width = pixels.length;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public Pixel getPixelAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or column out of range.");
    }
    return getCopyOfPixel(col, row);
  }

  protected Pixel getCopyOfPixel(int col, int row) {
    Pixel pixelToCopy = pixels[col][row]; //immutable -> can just return pixels[col][row]??

    return new RGBPixel(pixelToCopy.getComponent(ColorComponent.Red),
            pixelToCopy.getComponent(ColorComponent.Green),
            pixelToCopy.getComponent(ColorComponent.Blue));
  }

  protected Pixel[][] copyPixels() {
    Pixel[][] newPixels = new Pixel[this.width][this.height];

    for (int w = 0; w < this.width; w++) {
      for (int h = 0; h < this.height; h++) {
        newPixels[w][h] = getCopyOfPixel(w, h);
      }
    }

    return newPixels;
  }

  @Override
  public Image flipVertical() {
    Pixel[][] newPixels = this.copyPixels();
    for (int w = 0; w < this.width; w++) {
      List<Pixel> pixelsOfThisColumn = Arrays.asList(newPixels[w]);
      Collections.reverse(pixelsOfThisColumn);
      newPixels[w] = pixelsOfThisColumn.toArray(new Pixel[0]);
    }
    return new SimpleImage(newPixels);
  }

  @Override
  public Image flipHorizontal() {
    Pixel[][] newPixels = this.copyPixels();
    List<Pixel[]> columnsList = Arrays.asList(newPixels);
    Collections.reverse(columnsList);
    Pixel[][] flippedPixels = columnsList.toArray(new Pixel[0][0]);
    return new SimpleImage(flippedPixels);
  }

  @Override
  public Image adjustBrightness(int delta) {
    Pixel[][] newPixels = new Pixel[this.width][this.height];

    for (int w = 0; w < this.width; w++) {
      for (int h = 0; h < this.height; h++) {
        newPixels[w][h] = getCopyOfPixel(w, h).adjustBrightness(delta);
      }
    }

    return new SimpleImage(newPixels);
  }

  @Override
  public Image grayscale(ColorComponent comp) {
    Pixel[][] newPixels = new Pixel[this.width][this.height];

    for (int w = 0; w < this.width; w++) {
      for (int h = 0; h < this.height; h++) {
        newPixels[w][h] = getCopyOfPixel(w, h).grayscale(comp);
      }
    }

    return new SimpleImage(newPixels);
  }

  @Override
  public Image transformColor(double[][] matrix) throws IllegalArgumentException {
    Pixel[][] newPixels = new Pixel[this.width][this.height];

    for (int w = 0; w < this.width; w++) {
      for (int h = 0; h < this.height; h++) {
        newPixels[w][h] = getCopyOfPixel(w, h).transformColor(matrix);
      }
    }

    return new SimpleImage(newPixels);
  }

  @Override
  public Image filter(double[][] kernel) throws IllegalArgumentException {
    if (kernel == null) {
      throw new IllegalArgumentException("Given kernel was null.");
    }

    if (kernel.length % 2 == 0) {
      throw new IllegalArgumentException("Given kernel had an even dimension.");
    }

    if (kernel[0].length % 2 == 0) {
      throw new IllegalArgumentException("Given kernel had an even dimension.");
    }

    for (int i = 1; i < kernel.length; i++) {
      if (kernel[i] == null || kernel[0].length != kernel[i].length) {
        throw new IllegalArgumentException("Kernel was jagged.");
      }
    }

    Pixel[][] newPixels = new Pixel[this.width][this.height];

    for (int w = 0; w < this.width; w++) {
      for (int h = 0; h < this.height; h++) {
        newPixels[w][h] = this.resultOfKernel(w,h,kernel);
      }
    }

    return new SimpleImage(newPixels);
  }

  private Pixel resultOfKernel(int w, int h, double[][] kernel) {
    int topMostRow = h - (kernel[0].length / 2);
    int leftMostCol = w - (kernel.length / 2);

    double newR = 0.0;
    double newG = 0.0;
    double newB = 0.0;

    for (int r = 0; r < kernel.length; r++) {
      for (int c = 0; c < kernel[r].length; c++) {
        Pixel pixelHere;
        double kernelFactor = kernel[r][c];
        try {
          pixelHere = getCopyOfPixel(leftMostCol + c, topMostRow + r);
        } catch (ArrayIndexOutOfBoundsException e) {
          pixelHere = new RGBPixel(0,0,0);
        }

        newR += kernelFactor * pixelHere.getComponent(ColorComponent.Red);
        newG += kernelFactor * pixelHere.getComponent(ColorComponent.Green);
        newB += kernelFactor * pixelHere.getComponent(ColorComponent.Blue);
      }
    }

    int r = Math.min(255, Math.max(0, (int) Math.round(newR)));
    int g = Math.min(255, Math.max(0, (int) Math.round(newG)));
    int b = Math.min(255, Math.max(0, (int) Math.round(newB)));

    return new RGBPixel(r, g, b);
  }
}
