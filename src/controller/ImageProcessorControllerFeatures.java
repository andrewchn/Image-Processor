package controller;

public interface ImageProcessorControllerFeatures {
  void runCommand(String command);
  void loadImage(String filepath);
  void saveImage(String filepath);

}
