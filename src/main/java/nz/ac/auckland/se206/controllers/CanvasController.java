package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;
import nz.ac.auckland.se206.controllers.SoundsManager.bgm;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

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
public class CanvasController {

  @FXML
  private Pane root;

  @FXML
  private Canvas canvas;

  @FXML
  private Label targetWordLabel;

  @FXML
  private ProgressBar pgbTimer;

  @FXML
  private Label timerLabel;

  @FXML
  private Button readyButton;

  @FXML
  private Button clearButton;

  @FXML
  private Button penEraserButton;

  @FXML
  private Button saveDrawingButton;

  @FXML
  private PieChart modelResultsPieChart;

  @FXML
  private Label leaderBoardLabel;

  @FXML
  private ListView<String> leaderBoardList;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private String currentWord;

  private ObservableList<PieChart.Data> data;

  private Timeline timeline = new Timeline();

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
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

    // Initialise the canvas and disable it so users cannot draw on it
    initializeCanvas();
    pgbTimer.setVisible(false);
    pgbTimer.setStyle("-fx-accent: green;");
    canvas.setDisable(true);
    saveDrawingButton.setDisable(true);
    pgbTimer.setVisible(false);
    leaderBoardLabel.setVisible(false);
    leaderBoardList.setVisible(false);

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

  private void resetPieChart() {
    for (PieChart.Data data : modelResultsPieChart.getData()) {
      data.setName("");
      data.setPieValue(0);
    }
  }

  /** This method initialises the canvas at the start of the game */
  private void initializeCanvas() {
    graphic = canvas.getGraphicsContext2D();
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
          graphic.setFill(Color.BLACK);
          graphic.setLineWidth(size);

          // Select the pen or eraser depending on the value of the pen/eraser button text
          if (penEraserButton.getText().equals("Pen")) {
            // To erase lines, set the mouse to clear a small rectangle at the cursor
            // location
            graphic.clearRect(x, y, 10, 10);
          } else {
            // Create a line that goes from the point (currentX, currentY) and (x,y)
            graphic.strokeLine(currentX, currentY, x, y);
          }

          // update the coordinates
          currentX = x;
          currentY = y;
        });
    //Looping sound effects for pen/eraser
    canvas.setOnDragDetected(e ->{
      if (penEraserButton.getText().equals("Pen")) {
        SoundsManager.playBGM(bgm.ERASER);
      } else {
        SoundsManager.playBGM(bgm.PENCIL);
      }
    });
    canvas.setOnMouseReleased(e ->{
      if (penEraserButton.getText().equals("Pen")) {
        SoundsManager.stopBGM(bgm.ERASER);
      } else {
        SoundsManager.stopBGM(bgm.PENCIL);
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
        SoundsManager.playSFX(sfx.BUTTON2);
    // If the user is ready to draw, enable the canvas and save drawing button
    if (readyButton.getText().equals("Start!")) {
      SoundsManager.stopAllBGM();
      SoundsManager.playBGM(bgm.ZEN);
      // Always make sure progressbar is green at the start
      pgbTimer.setStyle("-fx-accent: green;");
      // Intiliase the canvas, enable the drawing buttons and disable the save drawing
      // button
      initializeCanvas();
      resetPieChart();
      readyButton.setDisable(true);
      saveDrawingButton.setDisable(true);
      clearButton.setDisable(false);
      pgbTimer.setVisible(true);
      modelResultsPieChart.setLegendVisible(true);
      // Disable leaderboard
      leaderBoardLabel.setVisible(false);
      leaderBoardList.setVisible(false);
      // Delegate the background tasks to different threads and execute these
      Task<Void> backgroundTimingTask = createNewTimingTask();
      pgbTimer.progressProperty().bind(backgroundTimingTask.progressProperty());
      Thread backgroundTimingThread = new Thread(backgroundTimingTask);
      backgroundTimingThread.setDaemon(true);
      backgroundTimingThread.start();

      Thread backgroundSpeechThread = new Thread(createNewSpeechTask());
      backgroundSpeechThread.setDaemon(true);
      backgroundSpeechThread.start();
    } else {
      model = new DoodlePrediction();
      // Clear the canvas, disable the save drawing button and clear the pie chart
      graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      saveDrawingButton.setDisable(true);

      selectWord();

      targetWordLabel.setText("The word to draw is: " + currentWord);
      timerLabel.setText("Press Start to start drawing!");
      readyButton.setText("Start!");
      text.add(currentWord); // Adds new randomWord, if current != random
    }
  }

  /**
   * This method executes when the user clicks the button to switch between pen
   * and eraser It
   * changes the text of the button to reflect the current tool
   */
  @FXML
  private void onSwitchBetweenPenAndEraser() {
    SoundsManager.playSFX(sfx.BUTTON2);
    // If the current tool is pen, change the text to reflect eraser and set the
    // current tool to
    // eraser and vice versa
    if (penEraserButton.getText().equals("Eraser")) {
      penEraserButton.setText("Pen");
    } else {
      penEraserButton.setText("Eraser");
    }
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

  private void selectWord() throws CsvException, IOException, URISyntaxException {
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    int wordsLevel = (int) gameSettings.getWordsLevel();

    // Get a random new word to draw, set the target world label and update the GUI
    CategorySelector categorySelector = new CategorySelector();
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);
    int randomNumber;

    switch (wordsLevel) {
      case 0:
        randomWord = categorySelector.getRandomCategory(Difficulty.E);
        break;
      case 1:
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
      case 2:
        randomNumber = (int) (Math.random() * (3 - 0) + 0);

        System.out.println(randomNumber);

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
      case 3:
        randomWord = categorySelector.getRandomCategory(Difficulty.H);
        break;
      default:
        randomWord = categorySelector.getRandomCategory(Difficulty.E);
        break;
    }

    if (randomWord.startsWith("\uFEFF")) {
      randomWord = randomWord.substring(1);
    }
    currentWord = randomWord;

    if (text.size() == categorySelector.getDifficultyMap().get(Difficulty.E).size()) {
      text.clear();
      randomWord = categorySelector.getRandomCategory(Difficulty.E);
      if (randomWord.startsWith("\uFEFF")) {
        randomWord = randomWord.substring(1);
      }
      currentWord = randomWord;
    }

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
    Image canvasContent = canvas.snapshot(null, null);
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
   * This method creates a background timing task and returns the task
   *
   * @return a Task<Void> object, the background timing task
   */
  private Task<Void> createNewTimingTask() {

    final AtomicInteger timeLeft = new AtomicInteger(getMaximumTime());
    Task<Void> backgroundTimingTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {

        // Up date the task progress
        updateProgress(0, getMaximumTime() - 1);
        /*
         * Initialise a timeline. This will be used to decrement the
         * timer every second, query the data learning
         * model and update the pie chart accordingly
         */
        KeyFrame keyFrame = new KeyFrame(
            Duration.seconds(1),
            e -> {
              try {
                timerLabel.setText(timeLeft.decrementAndGet() + " seconds left");
                updateProgress(getMaximumTime() - timeLeft.get(), getMaximumTime() - 1);
                // If game reaches last 10 second, change progress bar to red
                if (timeLeft.get() == 10) {
                  pgbTimer.setStyle("-fx-accent: red;");
                }
                // If game reaches last 10 second, start playing beep sfx

                // First check if the canvas is blank or not, if it's blank, reset the
                // piechart
                // Otherwise, carryout predictions and update piechart
                if (!isCanvasBlank()) {
                  // Query the DL model to make the predictions and update the pie chart
                  makePredictions();
                  /*
                   * If at any point the word to draw is in the top three
                   * predictions, the user has won the game. In this case
                   * stop the timeline, communicate to the user
                   * that they have won and allow them to choose a
                   * new word if they wish
                   */

                  if (isWordCorrect()) {
                    SoundsManager.stopAllBGM();
                    SoundsManager.playSFX(sfx.VICTORY);
                    pgbTimer.setVisible(false);
                    pgbTimer.progressProperty().unbind();
                    Stage stage = (Stage) root.getScene().getWindow();
                    StatisticsManager.setGameStage(stage);
                    timeline.stop();
                    try {
                      addLine("WON");
                      autoSaveDrawing();
                    } catch (IOException e1) {
                      e1.printStackTrace();
                    }
                    canvas.setOnMouseDragged((canvasEvent) -> {
                    });
                    canvas.setDisable(true);
                    timerLabel.setText("Correct, well done!");
                    readyButton.setDisable(false);
                    readyButton.setText("Ready?");
                    clearButton.setDisable(true);
                    saveDrawingButton.setDisable(false);
                    updateLeaderBoard();
                  }
                } else {
                  resetPieChart();
                }
              } catch (TranslateException | IOException e1) {
                e1.printStackTrace();
              }
            });
        KeyFrame beepFrame = new KeyFrame(
            Duration.seconds(1),
            e -> {
              if (timeLeft.get() <= 10&&timeLeft.get()>0) {
                SoundsManager.playSFX(sfx.BEEP);
              }
            });
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(keyFrame,beepFrame);
        timeline.setCycleCount(getMaximumTime());

        /*
         * When the one minute timer elapses, stop the timeline, disable the canvas and
         * drawing buttons, enable the save drawing button and check if the user
         * has won the game
         */
        timeline.setOnFinished(
            event -> {
              // Stop the timeline and reset the GUI to its initial state
              timeline.stop();
              Stage stage = (Stage) root.getScene().getWindow();
              StatisticsManager.setGameStage(stage);

              // Unbind and set progress bar to invisible
              pgbTimer.setVisible(false);
              pgbTimer.progressProperty().unbind();
              try {
                addLine("LOST");
                autoSaveDrawing();
              } catch (IOException e1) {
                e1.printStackTrace();
              }
              SoundsManager.stopAllBGM();
              SoundsManager.playSFX(sfx.FAIL);
              readyButton.setDisable(false);
              readyButton.setText("Ready?");
              clearButton.setDisable(true);
              canvas.setOnMouseDragged(e -> {
              });
              canvas.setDisable(true);
              saveDrawingButton.setDisable(false);

              // Check if the user has won and update the GUI to communicate to the user
              if (isWordCorrect()) {
                timerLabel.setText("Correct, well done!");
              } else {
                timerLabel.setText("Incorrect, better luck next time!");
                // Update leaderboard
                try {
                  updateLeaderBoard();
                } catch (IOException e1) {
                  e1.printStackTrace();
                }
              }
            });

        timeline.play();

        return null;
      }
    };
    return backgroundTimingTask;
  }

  private int getMaximumTime() {
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    return gameSettings.getTimeLevel();
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
   * This method checks if the user has won the game by checking if the word to
   * draw is in the top
   * three predictions
   *
   * @return a boolean, true if the current word is in the top three predictions,
   *         false otherwise
   */
  private boolean isWordCorrect() {
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    int accuracyLevel = (int) gameSettings.getAccuracyLevel();

    double predictionLevel = gameSettings.getPredictionLevel();

    switch (accuracyLevel) {
      case 0:
        // Check the top 3 entries in the pie chart and return true if the current word
        // is in the top 3
        // predictions
        for (int i = 0; i < 3; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)
              && modelResultsPieChart.getData().get(i).getPieValue() >= predictionLevel) {
            return true;
          }
        }
        return false;
      case 1:
        // Check the top 3 entries in the pie chart and return true if the current word
        // is in the top 3
        // predictions
        for (int i = 0; i < 2; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)
              && modelResultsPieChart.getData().get(i).getPieValue() >= predictionLevel) {
            return true;
          }
        }
        return false;
      case 2:
        // Check the top 3 entries in the pie chart and return true if the current word
        // is in the top 3
        // predictions
        for (int i = 0; i < 1; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)
              && modelResultsPieChart.getData().get(i).getPieValue() >= predictionLevel) {
            return true;
          }
        }
        return false;
      default:
        // Check the top 3 entries in the pie chart and return true if the current word
        // is in the top 3
        // predictions
        for (int i = 0; i < 3; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)
              && modelResultsPieChart.getData().get(i).getPieValue() >= predictionLevel) {
            return true;
          }
        }
        return false;
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

  private void addLine(String result) throws IOException {

    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    // Count the number of lines in the UserDatas.txt file
    Path path = Paths.get("DATABASE/UserDatas.txt");
    long count = Files.lines(path).count();
    int size = (int) count;

    // Format the line to be inputted into the file
    String linesCount = Files.readAllLines(path).get(size - 1);
    String line = currentWord + " , " + result + " , " + timerLabel.getText();
    FileWriter fileWriter;

    /*
     * Write the line to the file. If an IOException is raised, then
     * print out an error message to the console
     */
    try {
      fileWriter = new FileWriter("DATABASE/" + linesCount, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(
          line
              + " , "
              + gameSettings.getAccuracyLevel()
              + " , "
              + gameSettings.getWordsLevel()
              + " , "
              + gameSettings.getTimeSliderPosition()
              + " , "
              + gameSettings.getConfidenceSliderPosition());
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }

  private void autoSaveDrawing() throws IOException {

    final File autoSaveDrawingsFolder = new File("DATABASE/autosaves");

    if (!autoSaveDrawingsFolder.exists()) {
      autoSaveDrawingsFolder.mkdir();
    }

    final File canvasScreenshot = new File(("DATABASE/autosaves/" + currentWord) + ".png");

    ImageIO.write(getCurrentSnapshot(), "png", canvasScreenshot);
  }

  /**
   * Updates the leaderboard to constantly change user stats Worked as continous
   * append to previous
   * leaderboard
   *
   * @throws IOException If the updating leaderboard failed
   */
  @FXML
  private void updateLeaderBoard() throws IOException {
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();
    // Updates the leaderboard and receive previous leaderboard information
    leaderBoardList.getItems().clear();
    leaderBoardLabel.setVisible(true);
    leaderBoardList.setVisible(true);
    leaderBoardLabel.setText("Top artists of \n" + currentWord);
    ArrayList<Score> allScores;
    allScores = StatisticsManager.getLeaderBoard(currentWord);
    Score currentScore;
    for (int i = 0; i < 10; i++) { // Iterated to add new records to leaderboard
      if (i < allScores.size()) {
        currentScore = allScores.get(i);
        if (currentScore.getTime() == gameSettings.getTimeLevel() + 1) {
          leaderBoardList.getItems().add(currentScore.getID() + "  LOST");
        } else {
          leaderBoardList
              .getItems()
              .add(currentScore.getID() + "  " + currentScore.getTime() + " s");
        }
      } else {
        break;
      }
    }
  }

  // Resets the canvas automatically as well as the buttons
  void reset() {
    // Time stops, button enable/disabled, leaderboard and canvas update to new
    // value
    timeline.stop();
    readyButton.setDisable(false);
    readyButton.setText("Ready?");
    clearButton.setDisable(false);
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvas.setDisable(true);
    saveDrawingButton.setDisable(false);
    targetWordLabel.setText("Get a new word to begin drawing!");
    timerLabel.setText("");
    pgbTimer.setVisible(false);
    leaderBoardLabel.setVisible(false);
    leaderBoardList.setVisible(false);
  }
}
