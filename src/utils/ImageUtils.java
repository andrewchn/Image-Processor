package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import model.ColorComponent;
import model.Image;
import model.Pixel;
import model.RGBPixel;
import model.SimpleImage;


/**
 * This class contains utility methods to read a PPM image from file and record its contents as
 * an Image. It also contains a method to save an Image object as a PPM file.
 */
public class ImageUtils {

  /**
   * Read an image file in the PPM format return it as an Image.
   *
   * @param filename the path of the file.
   */
  public static Image readPPM(String filename)
          throws FileNotFoundException {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("File " + filename + " not found!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new FileNotFoundException("File found was not raw ppm");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    Pixel[][] pixels = new Pixel[width][height];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = (int) Math.round(sc.nextInt() * (255.0 / maxValue));
        int g = (int) Math.round(sc.nextInt() * (255.0 / maxValue));
        int b = (int) Math.round(sc.nextInt() * (255.0 / maxValue));
        pixels[j][i] = new RGBPixel(r, g, b);
      }
    }

    return new SimpleImage(pixels);
  }

  /**
   * Read an image file in a java supported image format and returns it as an Image.
   *
   * @param filename the path of the file.
   */
  public static Image readOther(String filename)
          throws IOException {

    File imageFile = new File(filename);

    if (!imageFile.exists()) {
      throw new FileNotFoundException("File " + filename + " not found!");
    }

    BufferedImage img = ImageIO.read(imageFile);

    int width = img.getWidth();
    int height = img.getHeight();

    Pixel[][] pixels = new Pixel[width][height];


    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int pixel = img.getRGB(j, i);
        Color color = new Color(pixel, true);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        pixels[j][i] = new RGBPixel(r, g, b);
      }
    }

    return new SimpleImage(pixels);
  }

  /**
   * Saves the information in an Image object as a PPM file which visually represents the Image.
   *
   * @param image the image to save.
   * @param name  the filepath to save the image to.
   * @throws IOException if there is an error writing to the file.
   */
  public static void savePPM(Image image, String name)
          throws IOException {

    FileWriter ppmWriter = new FileWriter(name);

    ppmWriter.write("P3\n#Created by Image Processor\n");

    ppmWriter.write(image.getWidth() + " " + image.getHeight() + "\n");

    ppmWriter.write("255\n");

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int r = image.getPixelAt(i, j).getComponent(ColorComponent.Red);
        int g = image.getPixelAt(i, j).getComponent(ColorComponent.Green);
        int b = image.getPixelAt(i, j).getComponent(ColorComponent.Blue);
        ppmWriter.write(r + "\n");
        ppmWriter.write(g + "\n");
        ppmWriter.write(b + "\n");
      }
    }

    ppmWriter.close();
  }

  /**
   * Saves the information in an Image object as a specified image type.
   * @param image Image to save.
   * @param name File name to save the image to.
   * @throws IOException If there is an error writing to the image file.
   */
  public static void saveOther(Image image, String name) throws IOException {
    BufferedImage bufferedImage = new BufferedImage(image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < bufferedImage.getHeight(); i++) {
      for (int j = 0; j < bufferedImage.getWidth(); j++) {
        Color c = new Color(image.getPixelAt(i, j).getComponent(ColorComponent.Red),
                image.getPixelAt(i, j).getComponent(ColorComponent.Green),
                image.getPixelAt(i, j).getComponent(ColorComponent.Blue));
        bufferedImage.setRGB(j, i, c.getRGB());
      }
    }

    File outputFile = new File(name);

    FileWriter fw = new FileWriter(outputFile);

    String fileType = getFileType(name);

    switch (fileType) {
      case "jpg":
      case "jpeg": //special case to write jpeg w/o compression
        ImageWriter writer = ImageIO.getImageWritersByFormatName(fileType).next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionType("JPEG");
        param.setCompressionQuality(1.0f); //retain best quality
        writer.setOutput(new FileImageOutputStream(outputFile));
        writer.write(null, new IIOImage(bufferedImage, null, null), param);
        break;
      default:
        ImageIO.write(bufferedImage, fileType, outputFile);
        break;
    }


  }

  private static String getFileType(String fileName) {
    return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
  }

}

