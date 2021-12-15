package view;
import controller.ImageProcessorControllerFeatures;
import model.Image;

public interface ImageProcessorGUIView {

  /**
   * sets the Image to display, and the corresponding histogram.
   * @param image to display.
   */
  void setImage(Image image);

  /**
   * Displays text message with title.
   * @param message to display.
   * @param title of message.
   */
  void showMessage(String message, String title);

  void passFeatures(ImageProcessorControllerFeatures controllerFeatures);
}
