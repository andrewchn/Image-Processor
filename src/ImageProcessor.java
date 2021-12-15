import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import controller.ImageProcessorController;
import controller.ImageProcessorControllerImpl;
import model.ImageProcessorModelImpl;
import view.ImageProcessorTextView;

/**
 * A program which allows for processing of JPG, BMP, PNG, and PPM images.
 */
public class ImageProcessor {

  /**
   * Runs the program, reading and writing input from the console.
   * @param args user can make the first argument the name of a file from which to run commands
   *             out of.
   * @throws FileNotFoundException if the file path in the first argument can not be found.
   */
  public static void main(String[] args) throws FileNotFoundException {

    Readable in;

    if (args.length > 0) {
      in = new FileReader(args[0]);
    } else {
      in = new InputStreamReader(System.in);
    }

    ImageProcessorController controller = new ImageProcessorControllerImpl(
            new ImageProcessorModelImpl(),
            in,
            new ImageProcessorTextView(System.out)
    );

    controller.activateProcessor();
  }
}
