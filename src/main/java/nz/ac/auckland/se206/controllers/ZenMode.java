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
import nz.ac.auckland.se206.controllers.SoundsManager.BackgroundMusic;
import nz.ac.auckland.se206.controllers.SoundsManager.SoundEffects;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class ZenMode {

  @FXML private Pane root;

  @FXML private Canvas drawingBoard;

  @FXML private Label targetWordLabel;

  @FXML private Button readyButton;

  @FXML private Button clearButton;

  @FXML private Button penEraserButton;

  @FXML private Button saveButton;

  @FXML private Button restartButton;

  @FXML private PieChart pieChartDisplay;

  @FXML private ColorPicker myColorPicker;

  @FXML private Button drawButton;

  @FXML private Button eraseButton;

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
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException If a string cannot be converted to a URI reference
   * @throws CsvException If there is an error in reading the input/output of the CSV file
   * @throws TranslateException It there is an error during processing of the input or output.
   */
  public void initialize()
      throws ModelException, IOException, CsvException, URISyntaxException, TranslateException {

    // Initialise the canvas and disable it so users cannot draw on it
    model = new DoodlePrediction();
    loadCanvas();

    /*
     * Set the initial visibilities of components and also set
     * the initial interactability of buttons
     */
    drawingBoard.setDisable(true);
    targetWordLabel.setText("Get a new word to begin drawing!");

    // Setting the buttons ready
    saveButton.setDisable(true);
    readyButton.setText("Ready?");

    // Initialise the data list for the model results pie chart
    data =
        FXCollections.observableArrayList(
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
    pieChartDisplay.setData(data);
    // Setting the Legend of pieChart
    pieChartDisplay.setLegendVisible(false);
    pieChartDisplay.setLegendSide(Side.LEFT);
  }

  /** This method resets the pie chart to a blank state */
  private void resetPieChart() {
    for (PieChart.Data data :
        pieChartDisplay.getData()) { // Retrieve all the pieChart display from last played and set
      // to 0
      data.setName("");
      data.setPieValue(0);
    }
  }

  /**
   * This method initialises the canvas at the start of the game
   *
   * @throws TranslateException It there is an error during processing of reading the input or
   *     output.
   */
  private void loadCanvas() throws TranslateException {

    graphic = drawingBoard.getGraphicsContext2D();
    // Disable draw and erase button
    drawButton.setDisable(true);
    eraseButton.setDisable(true);
    // save coordinates when mouse is pressed on the drawingBoard
    drawingBoard.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    drawingBoard.setOnMouseDragged(
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
    drawingBoard.setOnDragDetected(
        e -> {
          if (penOrEraser) {
            SoundsManager.playSoundEffects(SoundEffects.PENCIL);
          } else {
            SoundsManager.playSoundEffects(SoundEffects.ERASER);
          }
        });
    drawingBoard.setOnMouseReleased(
        e -> {
          if (penOrEraser) {
            SoundsManager.stopPencilOrEraserSoundEffects(SoundEffects.PENCIL);
          } else {
            SoundsManager.stopPencilOrEraserSoundEffects(SoundEffects.ERASER);
          }
        });

    drawingBoard.setDisable(false);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON2);
    graphic.clearRect(0, 0, drawingBoard.getWidth(), drawingBoard.getHeight());
  }

  /**
   * This method executes when the user clicks the "Ready" button. Every second, it gets the current
   * drawing, queries the DL model and updates the pie chart with the top 10 predictions of the DL
   * model along with the percentage certainty the model has in each prediction
   *
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   * @throws URISyntaxException If a string could not be parsed as a URI reference.
   * @throws IOException If there is an error in reading the input/output of the DL model.
   * @throws CsvException If there is an error regarding the CSV files opened using OpenCSV
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   */
  @FXML
  private void onReady()
      throws TranslateException, CsvException, IOException, URISyntaxException, ModelException {
    // Play pop button SoundEffects;
    SoundsManager.playSoundEffects(SoundEffects.BUTTON2);
    // If the user is ready to draw, enable the drawingBoard and save drawing button
    if (readyButton.getText().equals("Start!")) {
      // Update the ingame status
      InGameStatusManager.setInGameStatus(true);
      // Intiliase the canvas, enable the drawing buttons and disable the save drawing
      // button
      loadCanvas();
      // Enable the erase button, since the game is started with drawing ability
      eraseButton.setDisable(false);
      resetPieChart();
      // Play the senmode BGM
      SoundsManager.stopBackgroundMusic(BackgroundMusic.MAINPANEL);
      SoundsManager.playBackgroundMusic(BackgroundMusic.ZEN);
      readyButton.setDisable(true);
      saveButton.setDisable(true);
      clearButton.setDisable(false);
      pieChartDisplay.setLegendVisible(true);

      // Delegate the background tasks to different threads and execute these
      Thread backgroundSpeechThread = new Thread(generateNewSpeechTask());
      backgroundSpeechThread.setDaemon(true);
      backgroundSpeechThread.start();

      // Pie Chart prediction thread
      Thread backgroundTask = new Thread(showPrediction());
      backgroundTask.setDaemon(true);
      backgroundTask.start();

    } else {
      // SoundsManager.stopWinAndLoseSoundEffects();
      // // If zen mode bgm is not playing, then its safe to interrupt and play mainpanel
      // // bgm
      // if (!SoundsManager.isZenBackgroundMusicPlaying()) {
      //   SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
      // }

      // Clear the canvas, disable the save drawing button and clear the pie chart
      graphic.clearRect(0, 0, drawingBoard.getWidth(), drawingBoard.getHeight());
      saveButton.setDisable(true);
      randomWord();
      targetWordLabel.setText("The word to draw is: " + currentWord);
      readyButton.setText("Start!");
      text.add(currentWord); // Adds new randomWord, if current != random
    }
  }

  /**
   * This method is called when the user draws on the drawingBoard It draws a line on the
   * drawingBoard based on the chosen colour
   *
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  @FXML
  private void onDraw() throws TranslateException {
    // Play pop button SoundEffects;
    SoundsManager.playSoundEffects(SoundEffects.BUTTON2);
    drawButton.setDisable(true);
    eraseButton.setDisable(false);
    penOrEraser = true;
    graphic = drawingBoard.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the drawingBoard
    drawingBoard.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    drawingBoard.setOnMouseDragged(
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

    drawingBoard.setDisable(false);
  }

  /**
   * This method is called when the user draws on the drawingBoard in "Erase" mode It erases a line
   * on the drawingBoard based on the mouse position
   *
   * @throws TranslateException It there is an error during processing of reading the input or
   *     output of drawingBoard position
   */
  @FXML
  private void onErase() throws TranslateException {
    // Play pop button SoundEffects;
    SoundsManager.playSoundEffects(SoundEffects.BUTTON2);
    drawButton.setDisable(false);
    eraseButton.setDisable(true);
    penOrEraser = false;
    graphic = drawingBoard.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the drawingBoard
    drawingBoard.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    drawingBoard.setOnMouseDragged(
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

    drawingBoard.setDisable(false);
  }

  /**
   * This method creates a background speech task and returns the task
   *
   * @return a Task<Void> object, the background speech task
   */
  private Task<Void> generateNewSpeechTask() {
    Task<Void> backgroundSpeechTask =
        new Task<Void>() {

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
   * This method generates a random word to draw, depending on the difficulty selected by the user
   *
   * @throws CsvException Base class for all the exceptions for opencsv related work
   * @throws IOException If there is an error during processing of reading the input or output
   * @throws URISyntaxException If a string could not be parsed as a URI reference
   */
  private void randomWord() throws CsvException, IOException, URISyntaxException {

    // Get the user data and then get the current words difficulty
    Stage stage = (Stage) root.getScene().getWindow();
    Settings gameSettings = (Settings) stage.getUserData();
    int wordsLevel = (int) gameSettings.getWordsLevel();

    // Get a random new word to draw, set the target world label and update the GUI
    CategorySelector categoryPlatform = new CategorySelector();
    String randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
    int randomNumber;

    // Switch between the words level chosen by the user
    switch (wordsLevel) {
        // Easy mode: Choose only easy level words
      case 0:
        randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
        break;
        // Medium mode: Randomly choose easy or medium level words
      case 1:
        // Generate 0 or 1 randomly and choose an easy or medium word based on this
        // result
        randomNumber = (int) (Math.random() * (2 - 0) + 0);

        switch (randomNumber) {
          case 0:
            randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
            break;
          case 1:
            randomWord = categoryPlatform.getRandomCategory(Difficulty.M);
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
            randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
            break;
          case 1:
            randomWord = categoryPlatform.getRandomCategory(Difficulty.M);
            break;
          case 2:
            randomWord = categoryPlatform.getRandomCategory(Difficulty.H);
            break;
        }
        break;
        // Master mode: Choose only a hard word
      case 3:
        randomWord = categoryPlatform.getRandomCategory(Difficulty.H);
        break;
      default:
        randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
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
    if (text.size() == categoryPlatform.getDifficultyMap().get(Difficulty.E).size()) {
      text.clear();
      randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
      if (randomWord.startsWith("\uFEFF")) {
        randomWord = randomWord.substring(1);
      }
      currentWord = randomWord;
    }

    // If the randomly generated word has already been played, generate a new one
    while (text.contains(randomWord)) {
      randomWord = categoryPlatform.getRandomCategory(Difficulty.E);
      if (randomWord.startsWith("\uFEFF")) {
        randomWord = randomWord.substring(1);
      }
      currentWord = randomWord;
    }
  }

  /**
   * This method scan through the pixels on canvas Return true when canvas is blank, otherwise false
   */
  private Boolean isCanvasBlank() {
    // Get a snapshot of the current canvas
    Image canvasContent = drawingBoard.snapshot(null, null);

    // Scan through pixels on canvas
    for (int i = 0; i < drawingBoard.getHeight(); i++) {
      for (int j = 0; j < drawingBoard.getWidth(); j++) {
        if (canvasContent.getPixelReader().getArgb(j, i) != -1) {
          // If there is pixels that isn't blank, return false
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This method queries the data learning model to make the predictions and updates the pie chart
   * accordingly with the top 10 predictions made
   *
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  private void createPredictions() throws TranslateException {
    // Query the data learning model and get the top ten predictions
    List<Classification> predictionData = model.getPredictions(getImage(), 10);

    /*
     * Update the pie chart with the top ten predictions
     * Format the labels and values to be displayed in the pie chart
     * in a way that is more readable for users
     */
    for (int i = 0; i < predictionData.size(); i++) {
      data.get(i)
          .setName(
              predictionData.get(i).getClassName().replace("_", " ")
                  + ": "
                  + String.format("%.2f%%", 100 * predictionData.get(i).getProbability()));
      data.get(i).setPieValue(predictionData.get(i).getProbability());
    }
  }

  /**
   * Get the current image of the player's drawed on drawingBoard
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getImage() {

    // Got a snapshot view of the drawed image
    final Image snapshot = drawingBoard.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
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
   */
  @FXML
  private void onSaveCurrentSnapshotOnFile() throws IOException {

    // Open a save dialogue and allow the user to name the file and save it in a
    // custom location
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Drawing");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
    File file = fileChooser.showSaveDialog(drawingBoard.getScene().getWindow());

    if (file != null) {
      ImageIO.write(getImage(), "bmp", file);
    }
  }

  /**
   * This method is called when the user clicks on the "Restart" button. It resets the canvas and
   * the pie chart.
   */
  @FXML
  private void onRestart() {
    // Time stops, button enable/disabled, leaderboard and canvas update to new
    // value
    // Play button SoundEffects;
    SoundsManager.playSoundEffects(SoundEffects.BUTTON2);
    // Disable both draw/erase buttons
    drawButton.setDisable(true);
    eraseButton.setDisable(true);
    readyButton.setDisable(false);
    // Update the ingame status
    InGameStatusManager.setInGameStatus(false);
    readyButton.setText("Ready?");
    clearButton.setDisable(false);
    graphic.clearRect(0, 0, drawingBoard.getWidth(), drawingBoard.getHeight());
    drawingBoard.setDisable(true);
    saveButton.setDisable(false);
    targetWordLabel.setText("Get a new word to begin drawing!");
  }

  /**
   * This method is called to execute a background background task.
   *
   * @return The background task.
   * @throws TranslateException
   */
  private Task<Void> showPrediction() throws TranslateException {

    Task<Void> backgroundTask =
        new Task<Void>() {

          int clock = 1000000000;

          /**
           * This method is called when the background task is executed.
           *
           * @return null
           */
          @Override
          protected Void call() throws Exception {
            Timeline time = new Timeline();
            KeyFrame frame =
                new KeyFrame(
                    Duration.seconds(1),
                    new EventHandler<ActionEvent>() {

                      /**
                       * This method is invoked to activate the count down timer Every time seconds
                       * --, the integer value is converted to text in GUI Also, the top 10
                       * predictions and key features on button is disabled/enabled
                       */
                      public void handle(ActionEvent event) {
                        // Check the ingame status, if not, stop time line
                        if (!InGameStatusManager.isInGame()) {
                          time.stop();
                        }
                        clock--;
                        try {
                          createPredictions();
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

  /** This method is called when the user clicks the mouse. It plays a click sound effect */
  @FXML
  private void onMouseClicked() {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
  }

  /**
   * This method converts the colour code into readable 6 digit hexadecimal code and converts it
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
   * @throws NumberFormatException An error the application has attempted to convert a string to one
   *     of the numeric types, but string didn't had appropriate type.
   * @throws TranslateException It there is an error during processing of reading the input or
   *     output of hexadecimal code converting to red.
   */
  private int getRed() throws NumberFormatException, TranslateException {

    int r = Integer.valueOf(colorToHex().substring(0, 2), 16);
    return r;
  }

  /**
   * This method gets the green component of the RGB colour code
   *
   * @return The green component of the colour code
   * @throws NumberFormatException An error the application has attempted to convert a string to one
   *     of the numeric types, but string didn't had appropriate type.
   * @throws TranslateException If there is an error during processing of reading input or output
   */
  private int getGreen() throws NumberFormatException, TranslateException {
    int g = Integer.valueOf(colorToHex().substring(2, 4), 16);
    return g;
  }

  /**
   * This method gets the blue component of the RGB colour code
   *
   * @return The blue component of the colour code
   * @throws NumberFormatException An error the application has attempted to convert a string to one
   *     of the numeric types, but string didn't had appropriate type.
   * @throws TranslateException If there is an error during processing of reading input or output
   */
  private int getBlue() throws NumberFormatException, TranslateException {
    int b = Integer.valueOf(colorToHex().substring(4, 6), 16);
    return b;
  }
}
