package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ImageProcessorControllerFeatures;
import model.Image;
import utils.ImageUtils;

/**
 * This class opens the main window, that has different elements illustrated in
 * it. It also doubles up as all the listeners for simplicity. Such a design is
 * not recommended in general.
 */

public class ImageProcessorGraphicsView extends JFrame implements ActionListener, ImageProcessorGUIView {
  private JPanel mainPanel;
  private JScrollPane mainScrollPane;

  private JPanel imagesPanel;
  private JLabel imageLabel;
  private JLabel graphLabel;
  private JPanel editPanel;
  private JComboBox<String> combobox;
  private JButton visualizeButton;
  private JButton horzflipButton;
  private JButton blurButton;
  private JButton sharpenButton;
  private JButton loadButton;
  private JButton inputButton;
  private JButton vertflipButton;
  private JButton sepiaButton;
  private JButton grayscaleButton;
  private JButton fileSaveButton;
  private JPanel componentPanel;
  private JLabel inputDisplay;


  private ImageProcessorControllerFeatures controller;

  private Map<String, String> compMap;

  public ImageProcessorGraphicsView(ImageProcessorControllerFeatures controller) {
    super();

    this.controller = controller;
    this.createCompMap();

    setTitle("Swing features");
    setSize(1280, 800);


    mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //panel to hold image stuff
    imagesPanel = new JPanel();
    imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.X_AXIS));


    //show an image with a scrollbar
    JPanel imagePanel = new JPanel();
    //a border around the panel with a caption
    imagePanel.setBorder(BorderFactory.createTitledBorder("Image"));
    imagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    //imagePanel.setMaximumSize(null);
    imagesPanel.add(imagePanel);

    imageLabel = new JLabel();

    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageLabel.setIcon(new ImageIcon("Jellyfish.jpg"));
    imageScrollPane.setPreferredSize(new Dimension(100, 400));
    imagePanel.add(imageScrollPane);

    //show an image with a scrollbar
    JPanel graphPanel = new JPanel();
    //a border around the panel with a caption
    graphPanel.setBorder(BorderFactory.createTitledBorder("Component Histogram"));
    graphPanel.setLayout(new GridLayout(1, 0, 10, 10));
    //imagePanel.setMaximumSize(null);
    imagesPanel.add(graphPanel);

    graphLabel = new JLabel();

    JScrollPane graphScrollPane = new JScrollPane(graphLabel);
    graphLabel.setIcon(new ImageIcon());
    graphScrollPane.setPreferredSize(new Dimension(10, 100));
    graphPanel.add(graphScrollPane);

    mainPanel.add(imagesPanel);


    //panel for buttons below image
    editPanel = new JPanel();
    editPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    editPanel.setLayout(new GridLayout(2, 4));

    //panel to hold visualize stuff
    componentPanel = new JPanel();
    componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

    //component panel
    JPanel comboboxPanel = new JPanel();
    comboboxPanel.setLayout(new BoxLayout(comboboxPanel, BoxLayout.X_AXIS));
    componentPanel.add(comboboxPanel);

    String[] options = {"Red Component", "Green Component", "Blue Component",
            "Luma Component", "Value Component", "Intensity Component"};
    combobox = new JComboBox<String>();
    //the event listener when an option is selected
    combobox.setActionCommand("Component Options");
    combobox.addActionListener(this);
    for (int i = 0; i < options.length; i++) {
      combobox.addItem(options[i]);
    }

    comboboxPanel.add(combobox);

    //visualize
    JPanel visualizePanel = new JPanel();
    visualizePanel.setLayout(new FlowLayout());
    componentPanel.add(visualizePanel);
    visualizeButton = new JButton("Visualize Component");
    visualizeButton.setActionCommand("Visualize Component");
    visualizeButton.addActionListener(this);
    visualizePanel.add(visualizeButton);

    editPanel.add(componentPanel);

    //horizontal flip
    JPanel horzflipPanel = new JPanel();
    horzflipPanel.setLayout(new FlowLayout());
    editPanel.add(horzflipPanel);
    horzflipButton = new JButton("Flip Horizontally");
    horzflipButton.setActionCommand("horizontal-flip");
    horzflipButton.addActionListener(this);
    horzflipPanel.add(horzflipButton);

    //blur
    JPanel blurPanel = new JPanel();
    blurPanel.setLayout(new FlowLayout());
    editPanel.add(blurPanel);
    blurButton = new JButton("Blur");
    blurButton.setActionCommand("blur");
    blurButton.addActionListener(this);
    blurPanel.add(blurButton);

    //sharpen
    JPanel sharpenPanel = new JPanel();
    sharpenPanel.setLayout(new FlowLayout());
    editPanel.add(sharpenPanel);
    sharpenButton = new JButton("Sharpen");
    sharpenButton.setActionCommand("sharpen");
    sharpenButton.addActionListener(this);
    sharpenPanel.add(sharpenButton);

    //load image
    JPanel loadPanel = new JPanel();
    loadPanel.setLayout(new FlowLayout());
    editPanel.add(loadPanel);
    loadButton = new JButton("Load image");
    loadButton.setActionCommand("load");
    loadButton.addActionListener(this);
    loadPanel.add(loadButton);



    //JOptionsPane input dialog
    JPanel inputDialogPanel = new JPanel();
    inputDialogPanel.setLayout(new FlowLayout());
    editPanel.add(inputDialogPanel);

    inputButton = new JButton("Change Brightness");
    inputButton.setActionCommand("brighten");
    inputButton.addActionListener(this);
    inputDialogPanel.add(inputButton);

    inputDisplay = new JLabel("Default");
    inputDialogPanel.add(inputDisplay);

    //vertical flip
    JPanel vertflipPanel = new JPanel();
    vertflipPanel.setLayout(new FlowLayout());
    editPanel.add(vertflipPanel);
    vertflipButton = new JButton("Flip Vertically");
    vertflipButton.setActionCommand("vertical-flip");
    vertflipButton.addActionListener(this);
    vertflipPanel.add(vertflipButton);

    //sepia
    JPanel sepiaPanel = new JPanel();
    sepiaPanel.setLayout(new FlowLayout());
    editPanel.add(sepiaPanel);
    sepiaButton = new JButton("Sepia");
    sepiaButton.setActionCommand("sepia");
    sepiaButton.addActionListener(this);
    sepiaPanel.add(sepiaButton);

    //grayscale
    JPanel grayscalePanel = new JPanel();
    grayscalePanel.setLayout(new FlowLayout());
    editPanel.add(grayscalePanel);
    grayscaleButton = new JButton("Grayscale");
    grayscaleButton.setActionCommand("grayscale");
    grayscaleButton.addActionListener(this);
    grayscalePanel.add(grayscaleButton);

    //save image
    JPanel filesavePanel = new JPanel();
    filesavePanel.setLayout(new FlowLayout());
    editPanel.add(filesavePanel);
    fileSaveButton = new JButton("Save image");
    fileSaveButton.setActionCommand("save");
    fileSaveButton.addActionListener(this);
    filesavePanel.add(fileSaveButton);


    mainPanel.add(editPanel);

  }

  private void createCompMap() {
    this.compMap = new HashMap<String, String>();
    this.compMap.put("Red Component", "red-component");
    this.compMap.put("Blue Component", "blue-component");
    this.compMap.put("Green Component", "green-component");
    this.compMap.put("Luma Component", "luma-component");
    this.compMap.put("Intensity Component", "intensity-component");
    this.compMap.put("Value Component", "value-component");
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    switch (arg0.getActionCommand()) {
      case "Component Options": {
        String selected = (String) this.combobox.getSelectedItem();
        this.visualizeButton.setActionCommand(this.compMap.get(selected));
      }
      break;
      case "red-component":
      case "green-component":
      case "blue-component":
      case "value-component":
      case "intensity-component":
      case "luma-component":
      case "horizontal-flip":
      case "vertical-flip":
      case "blur":
      case "sharpen":
      case "grayscale":
      case "sepia":
        this.controller.runCommand(arg0.getActionCommand());
        break;
      case "load": {
        final JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG GIF PPM BMP PNG Images", "jpg", "gif","ppm","bmp","png");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(ImageProcessorGraphicsView.this);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          this.controller.loadImage(f.getAbsolutePath());
        }
      }
      break;
      case "save": {
        final JFileChooser fchooser = new JFileChooser(".");
        int retvalue = fchooser.showSaveDialog(ImageProcessorGraphicsView.this);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          this.controller.saveImage(f.getAbsolutePath());
        }
      }
      break;
      case "brighten":
        StringBuilder command = new StringBuilder("brightness ");
        boolean intCheck = false;
        int num = 0;
        do {
          intCheck = true;
          try {
            num = Integer.parseInt(JOptionPane.showInputDialog("Change brightness by: "));
          } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
            intCheck = false;
          }
        } while(!intCheck);

        inputDisplay.setText(num + "");
        command.append(num);

        this.controller.runCommand(command.toString());

        break;
    }
  }


  @Override
  public void setImage(Image image) {
    this.imageLabel.setIcon(ImageUtils.makeImageIcon(image));
    this.graphLabel.setIcon(new ImageIcon(ImageUtils.makeHistogram(image, 40, 4)));
  }

  @Override
  public void showMessage(String message, String title) {
    JOptionPane.showMessageDialog(ImageProcessorGraphicsView.this, message,
            "Message: " + title, JOptionPane.INFORMATION_MESSAGE);
  }

}
