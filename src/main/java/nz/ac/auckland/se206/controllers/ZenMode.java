package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

import nz.ac.auckland.se206.controllers.SoundsManager.bgm;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

/**
 * This is the controller of the canvas. You are free to modify this class and
 * the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict"
 * button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>
 * !! IMPORTANT !!
 *
 * <p>
 * Although we added the scale of the image, you need to be careful when
 * changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too
 * small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you
 * make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class ZenMode {

  @FXML
  private Pane root;

  @FXML
  private Canvas canvas;

  @FXML
  private Label targetWordLabel;

  @FXML
  private Button readyButton;

  @FXML
  private Button clearButton;

  @FXML
  private Button penEraserButton;

  @FXML
  private Button saveDrawingButton;

  @FXML
  private Button restartButton;

  @FXML
  private PieChart modelResultsPieChart;

  @FXML
  private ColorPicker myColorPicker;

  @FXML
  private Button drawButton;

  @FXML
  private Button eraseButton;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private String currentWord;

  private ObservableList<PieChart.Data> data;

  private Boolean penOrEraser;

  // mouse coordinates
  private double currentX;
  private double currentY;

  private ArrayList<String> text = new ArrayList<String>(); // Create an ArrayList object

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we
   * create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException     If there is an error in reading the input/output
   *                            of the DL model.
   * @throws IOException        If the model cannot be found on the file system.
   * @throws URISyntaxException
   * @throws CsvException
   * @throws TranslateException It there is an error during processing of the
   *                            input or output.
   */
  public void initialize()
      throws ModelException, IOException, CsvException, URISyntaxException, TranslateException {

    // Initialise the canvas and disable it so users cannot draw on it
    initializeCanvas();

    /*
     * Set the initial visibilities of components and also set
     * the initial interactability of buttons
     */
    canvas.setDisable(true);
    saveDrawingButton.setDisable(true);
    targetWordLabel.setText("Get a new word to begin drawing!");
    readyButton.setText("Ready?");

    // Initialise the data list for the model results pie chart
    data = FXCollections.observableArrayList(
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0),
        new PieChart.Data("", 0));

    // Set the data list for the model results pie chart and initialise the legend
    modelResultsPieChart.setData(data);
    modelResultsPieChart.setLegendSide(Side.LEFT);
    modelResultsPieChart.setLegendVisible(false);
  }

  /** This method resets the pie chart to a blank state */
  private void resetPieChart() {
    for (PieChart.Data data : modelResultsPieChart.getData()) {
      data.setName("");
      data.setPieValue(0);
    }
  }

  /**
   * This method initialises the canvas at the start of the game
   *
   * @throws TranslateException It there is an error during processing of reading
   *                            the input or output.
   * 
   */
  private void initializeCanvas() throws TranslateException {

    graphic = canvas.getGraphicsContext2D();
    // Disable draw and erase button
    drawButton.setDisable(true);
    eraseButton.setDisable(true);
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          try {
            graphic.setStroke(Color.rgb(getRed(), getGreen(), getBlue()));
          } catch (NumberFormatException | TranslateException e1) {
            e1.printStackTrace();
          }
          graphic.setLineWidth(size);
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
    penOrEraser = true;
    // Looping sound effects for pen/eraser
    canvas.setOnDragDetected(e -> {
      if (penOrEraser) {
        SoundsManager.playSFX(sfx.PENCIL);
      } else {
        SoundsManager.playSFX(sfx.ERASER);
      }
    });
    canvas.setOnMouseReleased(e -> {
      if (penOrEraser) {
        SoundsManager.stopPencilOrEraserSFX(sfx.PENCIL);
      } else {
        SoundsManager.stopPencilOrEraserSFX(sfx.ERASER);
      }
    });

    canvas.setDisable(false);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    SoundsManager.playSFX(sfx.BUTTON2);
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * This method executes when the user clicks the "Ready" button. Every second,
   * it gets the current
   * drawing, queries the DL model and updates the pie chart with the top 10
   * predictions of the DL
   * model along with the percentage certainty the model has in each prediction
   *
   * @throws TranslateException If there is an error in reading the input/output
   *                            of the DL model.
   * @throws URISyntaxException If a string could not be parsed as a URI
   *                            reference.
   * @throws IOException        If there is an error in reading the input/output
   *                            of the DL model.
   * @throws CsvException       If there is an error regarding the CSV files
   *                            opened using OpenCSV
   * @throws ModelException
   */
  @FXML
  private void onReady()
      throws TranslateException, CsvException, IOException, URISyntaxException, ModelException {
    // Play pop button sfx
    SoundsManager.playSFX(sfx.BUTTON2);
    // If the user is ready to draw, enable the canvas and save drawing button
    if (readyButton.getText().equals("Start!")) {
      // Intiliase the canvas, enable the drawing buttons and disable the save drawing
      // button
      initializeCanvas();
      // Enable the erase button, since the game is started with drawing ability
      eraseButton.setDisable(false);
      resetPieChart();
      // Play the senmode BGM
      SoundsManager.stopBGM(bgm.MAINPANEL);
      SoundsManager.playBGM(bgm.ZEN);
      readyButton.setDisable(true);
      saveDrawingButton.setDisable(true);
      clearButton.setDisable(false);
      modelResultsPieChart.setLegendVisible(true);

      // Delegate the background tasks to different threads and execute these
      Thread backgroundSpeechThread = new Thread(createNewSpeechTask());
      backgroundSpeechThread.setDaemon(true);
      backgroundSpeechThread.start();

      // Pie Chart prediction thread
      Thread backgroundTask = new Thread(showPrediction());
      backgroundTask.setDaemon(true);
      backgroundTask.start();

    } else {
      SoundsManager.stopWinAndLoseSFX();
      // If zen mode bgm is not playing, then its safe to interrupt and play mainpanel
      // bgm
      if (!SoundsManager.isZenBGMPlaying()) {
        SoundsManager.playBGM(bgm.MAINPANEL);
      }
      model = new DoodlePrediction();
      // Clear the canvas, disable the save drawing button and clear the pie chart
      graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      saveDrawingButton.setDisable(true);
      selectWord();
      targetWordLabel.setText("The word to draw is: " + currentWord);
      readyButton.setText("Start!");
      text.add(currentWord); // Adds new randomWord, if current != random
    }
  }

  /**
   * This method is called when the user draws on the canvas It draws a line on
   * the canvas based on
   * the chosen colour
   *
   * @throws TranslateException
   */
  @FXML
  private void onDraw() throws TranslateException {
    // Play pop button sfx
    SoundsManager.playSFX(sfx.BUTTON2);
    drawButton.setDisable(true);
    eraseButton.setDisable(false);
    penOrEraser = true;
    graphic = canvas.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          try {
            graphic.setStroke(Color.rgb(getRed(), getGreen(), getBlue()));
          } catch (NumberFormatException | TranslateException e1) {
            e1.printStackTrace();
          }
          graphic.setLineWidth(size);
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });

    canvas.setDisable(false);
  }

  /**
   * This method is called when the user draws on the canvas in "Erase" mode It
   * erases a line on the
   * canvas based on the mouse position
   *
   * @throws TranslateException It there is an error during processing of reading
   *                            the
   *                            input or output of canvas position
   */
  @FXML
  private void onErase() throws TranslateException {
    // Play pop button sfx
    SoundsManager.playSFX(sfx.BUTTON2);
    drawButton.setDisable(false);
    eraseButton.setDisable(true);
    penOrEraser = false;
    graphic = canvas.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 8;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          try {
            graphic.setStroke(Color.rgb(255, 255, 255));
          } catch (NumberFormatException e1) {
            e1.printStackTrace();
          }
          graphic.setLineWidth(size);
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });

    canvas.setDisable(false);
  }

  /**
   * This method creates a background speech task and returns the task
   *
   * @return a Task<Void> object, the background speech task
   */
  private Task<Void> createNewSpeechTask() {
    Task<Void> backgroundSpeechTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {

        // Use text to speech to communicate the current word to draw
        TextToSpeech textToSpeech = new TextToSpeech();
        textToSpeech.speak("The word to draw is" + currentWord);

        return null;
      }
    };

    return backgroundSpeechTask;
  }

  /**
   * This method generates a random word to draw, depending on the difficulty
   * selected by the user
   *
   * @throws CsvException       Base class for all the exceptions for opencsv
   *                            related work
   * @throws IOException
   * @throws URISyntaxException
   */
  private void selectWord() throws CsvException, IOException, URISyntaxException {

    // Get the user data and then get the current words difficulty
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    int wordsLevel = (int) gameSettings.getWordsLevel();

    // Get a random new word to draw, set the target world label and update the GUI
    CategorySelector categorySelector = new CategorySelector();
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);
    int randomNumber;

    // Switch between the words level chosen by the user
    switch (wordsLevel) {
      // Easy mode: Choose only easy level words
      case 0:
        randomWord = categorySelector.getRandomCategory(Difficulty.E);
        break;
      // Medium mode: Randomly choose easy or medium level words
      case 1:
        // Generate 0 or 1 randomly and choose an easy or medium word based on this
        // result
        randomNumber = (int) (Math.random() * (2 - 0) + 0);

        switch (randomNumber) {
          case 0:
            randomWord = categorySelector.getRandomCategory(Difficulty.E);
            break;
          case 1:
            randomWord = categorySelector.getRandomCategory(Difficulty.M);
            break;
        }
        break;
      // Hard mode: Randomly choose easy, medium or hard level words
      case 2:

        // Generate 0, 1 or 2 randomly and choose an easy, medium or hard word based on
        // this result
        randomNumber = (int) (Math.random() * (3 - 0) + 0);

        switch (randomNumber) {
          case 0:
            randomWord = categorySelector.getRandomCategory(Difficulty.E);
            break;
          case 1:
            randomWord = categorySelector.getRandomCategory(Difficulty.M);
            break;
          case 2:
            randomWord = categorySelector.getRandomCategory(Difficulty.H);
            break;
        }
        break;
      // Master mode: Choose only a hard word
      case 3:
        randomWord = categorySelector.getRandomCategory(Difficulty.H);
        break;
      default:
        randomWord = categorySelector.getRandomCategory(Difficulty.E);
        break;
    }

    // If the chosen word has a prefix then remove this prefix
    if (randomWord.startsWith("\uFEFF")) {
      randomWord = randomWord.substring(1);
    }
    currentWord = randomWord;

    // If all the words in the easy category have been played, reset the words seen
    // and choose a
    // random word
    if (text.size() == categorySelector.getDifficultyMap().get(Difficulty.E).size()) {
      text.clear();
      randomWord = categorySelector.getRandomCategory(Difficulty.E);
      if (randomWord.startsWith("\uFEFF")) {
        randomWord = randomWord.substring(1);
      }
      currentWord = randomWord;
    }

    // If the randomly generated word has already been played, generate a new one
    while (text.contains(randomWord)) {
      randomWord = categorySelector.getRandomCategory(Difficulty.E);
      if (randomWord.startsWith("\uFEFF")) {
        randomWord = randomWord.substring(1);
      }
      currentWord = randomWord;
    }
  }

  /**
   * This method scan through the pixels on canvas Return true when canvas is
   * blank, otherwise false
   */
  private Boolean isCanvasBlank() {
    // Get a snapshot of the current canvas
    Image canvasContent = canvas.snapshot(null, null);

    // Scan through pixels on canvas
    for (int i = 0; i < canvas.getHeight(); i++) {
      for (int j = 0; j < canvas.getWidth(); j++) {
        if (canvasContent.getPixelReader().getArgb(j, i) != -1) {
          // If there is pixels that isn't blank, return false
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This method queries the data learning model to make the predictions and
   * updates the pie chart
   * accordingly with the top 10 predictions made
   *
   * @throws TranslateException If there is an error in reading the input/output
   *                            of the DL model.
   */
  private void makePredictions() throws TranslateException {
    // Query the data learning model and get the top ten predictions
    List<Classification> predictionResults = model.getPredictions(getCurrentSnapshot(), 10);

    /*
     * Update the pie chart with the top ten predictions
     * Format the labels and values to be displayed in the pie chart
     * in a way that is more readable for users
     */
    for (int i = 0; i < predictionResults.size(); i++) {
      data.get(i)
          .setName(
              predictionResults.get(i).getClassName().replace("_", " ")
                  + ": "
                  + String.format("%.2f%%", 100 * predictionResults.get(i).getProbability()));
      data.get(i).setPieValue(predictionResults.get(i).getProbability());
    }
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    graphics.dispose();

    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   * 
   */
  @FXML
  private void onSaveCurrentSnapshotOnFile() throws IOException {

    // Open a save dialogue and allow the user to name the file and save it in a
    // custom location
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Drawing");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
    File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

    if (file != null) {
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
    }
  }

  /**
   * This method is called when the user clicks on the "Restart" button. It resets
   * the canvas and
   * the pie chart.
   */
  @FXML
  private void onRestart() {
    // Time stops, button enable/disabled, leaderboard and canvas update to new
    // value
    // Play button sfx
    SoundsManager.playSFX(sfx.BUTTON2);
    // Disable both draw/erase buttons
    drawButton.setDisable(true);
    eraseButton.setDisable(true);
    readyButton.setDisable(false);
    readyButton.setText("Ready?");
    clearButton.setDisable(false);
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvas.setDisable(true);
    saveDrawingButton.setDisable(false);
    targetWordLabel.setText("Get a new word to begin drawing!");
  }

  /**
   * This method is called to execute a background background task.
   *
   * @return The background task.
   * @throws TranslateException
   */
  private Task<Void> showPrediction() throws TranslateException {

    Task<Void> backgroundTask = new Task<Void>() {

      int clock = 1000000000;

      /**
       * This method is called when the background task is executed.
       *
       * @return null
       */
      @Override
      protected Void call() throws Exception {
        Timeline time = new Timeline();
        KeyFrame frame = new KeyFrame(
            Duration.seconds(1),
            new EventHandler<ActionEvent>() {

              /**
               * This method is invoked to activate the count down timer Every time seconds
               * --, the integer value is converted to text in GUI Also, the top 10
               * predictions and key features on button is disabled/enabled
               */
              public void handle(ActionEvent event) {
                clock--;
                try {
                  makePredictions();
                  colorToHex();
                  if (isCanvasBlank()) {
                    resetPieChart();
                  }
                } catch (TranslateException e2) {
                  e2.printStackTrace();
                }

                if (clock == 0) { // Once counter reach 0, every feature disabled except
                  time.stop();
                }
              }
            });

        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(frame);
        if (time != null) {
          time.stop();
        }
        time.play();
        {
          return null;
        }
      }
    };

    return backgroundTask;
  }

  @FXML
  private void onMouseClicked() {
    SoundsManager.playSFX(sfx.BUTTON1);
  }

  /**
   * This method converts the colour code into readable 6 digit hexadecimal code
   * and converts it
   * into R, G, B integer value.
   *
   * @return The hex value of the colour code
   */
  private String colorToHex() throws TranslateException {

    String colourString = myColorPicker.getValue().toString();
    return colourString.substring(2, 8);
  }

  /**
   * This method gets the red component of the RGB colour code
   *
   * @return The red component of the colour code
   * @throws NumberFormatException
   * @throws TranslateException    It there is an error during processing of
   *                               reading the
   *                               input or output of hexadecimal code converting
   *                               to red.
   */
  private int getRed() throws NumberFormatException, TranslateException {

    int r = Integer.valueOf(colorToHex().substring(0, 2), 16);
    return r;
  }

  /**
   * This method gets the green component of the RGB colour code
   *
   * @return The green component of the colour code
   * @throws NumberFormatException An error the application has attempted to
   *                               convert
   *                               a string to one of the numeric types, but
   *                               string didn't had appropriate type.
   * @throws TranslateException
   */
  private int getGreen() throws NumberFormatException, TranslateException {
    int g = Integer.valueOf(colorToHex().substring(2, 4), 16);
    return g;
  }

  /**
   * This method gets the blue component of the RGB colour code
   *
   * @return The blue component of the colour code
   * @throws NumberFormatException
   * @throws TranslateException
   */
  private int getBlue() throws NumberFormatException, TranslateException {
    int b = Integer.valueOf(colorToHex().substring(4, 6), 16);
    return b;
  }
}
