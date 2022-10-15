package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;

import javafx.application.Platform;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

import javafx.animation.TranslateTransition;
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
  private Button arrowUp;

  @FXML
  private Button arrowDown;

  @FXML
  private ImageView imageUp;

  @FXML
  private ImageView imageDown;

  @FXML
  private Label hint1Label;

  @FXML
  private Label hint2Label;

  @FXML
  private Button hintButton;

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

  TranslateTransition movementUp = new TranslateTransition();

  TranslateTransition movementDown = new TranslateTransition();

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
    model = new DoodlePrediction();
    /*
     * Set the initial visibilities of components and also set
     * the initial interactability of buttons
     */
    hintButton.setVisible(HiddenWordFunctions.isHiddenMode());
    hintButton.setDisable(true);
    hint1Label.setVisible(false);
    hint2Label.setVisible(false);
    pgbTimer.setVisible(false);
    pgbTimer.setStyle("-fx-accent: green;");
    canvas.setDisable(true);
    saveDrawingButton.setDisable(true);
    pgbTimer.setVisible(false);
    leaderBoardLabel.setVisible(false);
    leaderBoardList.setVisible(false);
    // If we're not entering hidden mode, update targetWord label as usual
    if (!HiddenWordFunctions.isHiddenMode()) {
      targetWordLabel.setText("Get a new word to begin drawing!");
    } else {
      // If we're in hidden mode, use special message
      targetWordLabel.setText("Welcome to Hidden word mode !");
    }
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
    movementUp.setFromX(currentX);
    imageUp.setVisible(false);
    imageDown.setVisible(false);
    resetArrow();
  }

  /** This method resets the pie chart to a blank state */
  private void resetPieChart() {
    for (PieChart.Data data : modelResultsPieChart.getData()) {
      data.setName("");
      data.setPieValue(0);
    }
  }

  /** This method initialises the canvas at the start of the game */
  private void initializeCanvas() {
    // Initialise the canvas, clear it and set the graphics context
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
    // Looping sound effects for pen/eraser
    canvas.setOnDragDetected(e -> {
      if (penEraserButton.getText().equals("Pen")) {
        SoundsManager.playSFX(sfx.ERASER);
      } else {
        SoundsManager.playSFX(sfx.PENCIL);
      }
    });
    canvas.setOnMouseReleased(e -> {
      if (penEraserButton.getText().equals("Pen")) {
        SoundsManager.stopPencilOrEraserSFX(sfx.ERASER);
      } else {
        SoundsManager.stopPencilOrEraserSFX(sfx.PENCIL);
      }
    });
    canvas.setDisable(false);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    SoundsManager.playSFX(sfx.BUTTON2);
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    resetArrow();
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
    SoundsManager.stopWinAndLoseSFX();
    SoundsManager.playSFX(sfx.BUTTON2);
    // If the user is ready to draw, enable the canvas and save drawing button
    if (readyButton.getText().equals("Start!")) {
      hintButton.setDisable(false);
      targetWordLabel.setVisible(true);
      SoundsManager.stopAllBGM();
      if (!HiddenWordFunctions.isHiddenMode()) {
        SoundsManager.playBGM(bgm.INGAME);
      } else {
        SoundsManager.playBGM(bgm.HIDDEN);
      }
      // Always make sure progressbar is green at the start
      pgbTimer.setStyle("-fx-accent: green;");
      // Intiliase the canvas and reset the pie chart
      initializeCanvas();
      resetPieChart();

      // Set interactability of buttons and set the pie chart legend to be visible
      readyButton.setDisable(true);
      saveDrawingButton.setDisable(true);
      clearButton.setDisable(false);
      pgbTimer.setVisible(true);
      modelResultsPieChart.setLegendVisible(true);
      imageUp.setVisible(true);
      imageDown.setVisible(true);

      // Disable leaderboard
      leaderBoardLabel.setVisible(false);
      leaderBoardList.setVisible(false);

      // Delegate background timing to a thread and execute this task
      Task<Void> backgroundTimingTask = createNewTimingTask();
      pgbTimer.progressProperty().bind(backgroundTimingTask.progressProperty());
      Thread backgroundTimingThread = new Thread(backgroundTimingTask);
      backgroundTimingThread.setDaemon(true);
      backgroundTimingThread.start();
      if (!HiddenWordFunctions.isHiddenMode()) {
        // Delegate background speech to a thread and execute this task
        Thread backgroundSpeechThread = new Thread(createNewSpeechTask());
        backgroundSpeechThread.setDaemon(true);
        backgroundSpeechThread.start();
      }
    } else {
      SoundsManager.stopWinAndLoseSFX();
      SoundsManager.playBGM(bgm.MAINPANEL);
      hintButton.setText("Hint 1?");
      // Clear the canvas, disable the save drawing button and clear the pie chart
      graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      saveDrawingButton.setDisable(true);

      // Get a new random word to draw
      selectWord();
      if (!HiddenWordFunctions.isHiddenMode()) {
        // Update the GUI to communicate the word to draw with the user
        targetWordLabel.setFont(Font.font("Lucida Fax Regular", FontWeight.NORMAL, 25));
        targetWordLabel.setTextAlignment(TextAlignment.CENTER);
        timerLabel.setText("Press Start to start drawing!");
      } else {
        Task<Void> getDefinition = new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            String def = HiddenWordFunctions.searchWordDefinetion(currentWord).get(0);
            Platform.runLater(() -> {
              targetWordLabel.setFont(Font.font("Lucida Fax Regular", FontWeight.NORMAL, 15));
              targetWordLabel.setTextAlignment(TextAlignment.LEFT);
              targetWordLabel.setText(def);
            });
            return null;
          }
        };
        getDefinition.setOnScheduled(e -> {
          Platform.runLater(() -> {
            timerLabel.setText("Preparing the word definitions for you...");
            readyButton.setDisable(true);
          });
        });
        getDefinition.setOnSucceeded(e -> {
          Platform.runLater(() -> {
            readyButton.setDisable(false);
            timerLabel.setText("Press Start to start drawing!");
          });
        });
        Thread backgroundDefinitionSearch = new Thread(getDefinition);
        backgroundDefinitionSearch.setDaemon(true);
        backgroundDefinitionSearch.start();
        targetWordLabel.setVisible(false);
      }
      hint1Label.setVisible(false);
      hint2Label.setVisible(false);
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

  /**
   * This method generates a random word to draw, depending on the difficulty
   * selected by the user
   *
   * @throws CsvException
   * @throws IOException
   * @throws URISyntaxException
   */
  private void selectWord() throws CsvException, IOException, URISyntaxException {

    // Get the uesr data and then get the current words difficulty
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    int wordsLevel = (int) gameSettings.getWordsLevel();

    // Get a random new word to draw, set the target world label and update the GUI
    CategorySelector categorySelector = new CategorySelector();
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);
    int randomNumber;

    // Switch between the level chosen by the user
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

  @FXML
  private void onHint() {
    if (hintButton.getText().equals("Hint 1?")) {
      hintButton.setText("Hint 2?");
      hint1Label.setVisible(true);
      hint1Label.setText("The word is " + currentWord.length() + " letters long");
    } else {
      hint2Label.setVisible(true);
      hint2Label.setText("The word begins with letter " + currentWord.charAt(0));
      hintButton.setDisable(true);
    }
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

        // Update the task progress
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
                  if (insideTopPrediction()) {
                    arrowMotionControlUp();
                  }

                  if (!insideTopPrediction()) {
                    arrowMotionControlDown();
                  }

                  if (isWordCorrect()) {
                    // Stop bgms and paly victory sfx
                    SoundsManager.stopAllBGM();
                    SoundsManager.playSFX(sfx.VICTORY);
                    // Update GUI elements
                    pgbTimer.setVisible(false);
                    pgbTimer.progressProperty().unbind();
                    resetArrow();

                    // Pass the current stage to the StatisticsManafer class
                    Stage stage = (Stage) root.getScene().getWindow();
                    StatisticsManager.setGameStage(stage);
                    timeline.stop();
                    try {
                      // Record a won game and auto-save the drawing
                      addLine("WON");
                      autoSaveDrawing();
                    } catch (IOException e1) {
                      e1.printStackTrace();
                    }

                    // Stop the user from drawing on the canvas, and update the GUI
                    canvas.setOnMouseDragged((canvasEvent) -> {
                    });
                    canvas.setDisable(true);
                    if (!HiddenWordFunctions.isHiddenMode()) {
                      timerLabel.setText("Correct, well done!");
                    } else {
                      timerLabel.setText("The word to draw is " + currentWord + ", correct, well done!");
                    }
                    readyButton.setDisable(false);
                    readyButton.setText("Ready?");
                    clearButton.setDisable(true);
                    saveDrawingButton.setDisable(false);
                    // Display and update the leader board
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
              if (timeLeft.get() <= 10 && timeLeft.get() > 0) {
                SoundsManager.playSFX(sfx.BEEP);
              }
            });
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(keyFrame, beepFrame);
        timeline.setCycleCount(getMaximumTime());

        /*
         * When the maximum time elapses, stop the timeline, disable the canvas and
         * drawing buttons, enable the save drawing button and check if the user
         * has won the game
         */
        timeline.setOnFinished(
            event -> {
              // Stop the timeline and reset the GUI to its initial state
              timeline.stop();
              resetArrow();
              Stage stage = (Stage) root.getScene().getWindow();
              StatisticsManager.setGameStage(stage);

              // Unbind and set progress bar to invisible
              pgbTimer.setVisible(false);
              pgbTimer.progressProperty().unbind();
              try {
                // Record a won game and auto-save the drawing
                addLine("LOST");
                autoSaveDrawing();
              } catch (IOException e1) {
                e1.printStackTrace();
              }
              // Stop all bgms and play failing sfx
              SoundsManager.stopAllBGM();
              SoundsManager.playSFX(sfx.FAIL);
              // Stop the user from drawing on the canvas, and update the GUI
              readyButton.setDisable(false);
              readyButton.setText("Ready?");
              clearButton.setDisable(true);
              canvas.setOnMouseDragged(e -> {
              });
              canvas.setDisable(true);
              saveDrawingButton.setDisable(false);

              // Check if the user has won and update the GUI to communicate to the user
              if (isWordCorrect()) {
                if (!HiddenWordFunctions.isHiddenMode()) {
                  timerLabel.setText("Correct, well done!");
                } else {
                  timerLabel.setText("The word to draw is " + currentWord + ", correct, well done!");
                }
              } else {
                if (!HiddenWordFunctions.isHiddenMode()) {
                  timerLabel.setText("Incorrect, better luck next time!");
                } else {
                  timerLabel.setText("The word to draw is " + currentWord + ", incorrect, better luck next time!");
                }
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

  /**
   * This method returns the current maximum allowable time based on the
   * difficulty chosen by the
   * user
   *
   * @return The maximum allowable time of type Integer
   */
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

    // Get the user data and then get the accuracy level and prediction level
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

  /**
   * This method writes the line for the game played to the corresponding user
   * file
   *
   * @param result The outcome of the game, either "WON" or "LOST"
   * @throws IOException
   */
  private void addLine(String result) throws IOException {

    // Get the user data
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    String currentLine;
    String lastLine = "";
    String[] separatedUserInfo = { "" };

    // Read the current user file
    BufferedReader bufferedReader = new BufferedReader(
        new FileReader("DATABASE/usersettings/" + gameSettings.getCurrentUser()));

    // Get the last line of the user file
    while ((currentLine = bufferedReader.readLine()) != null) {
      lastLine = currentLine;
    }

    bufferedReader.close();

    separatedUserInfo = lastLine.split(" , ");

    int maximumTime = 60;

    // Set the maximum time allowed based on the user settings
    switch ((int) Double.parseDouble(separatedUserInfo[2])) {
      case 0:
        maximumTime = 60;
        break;
      case 1:
        maximumTime = 45;
        break;
      case 2:
        maximumTime = 30;
        break;
      case 3:
        maximumTime = 15;
        break;
      default:
        maximumTime = 60;
        break;
    }

    // Count the number of lines in the UserDatas.txt file
    Path path = Paths.get("DATABASE/UserDatas.txt");
    long count = Files.lines(path).count();
    int size = (int) count;

    // Format the line to be inputted into the file
    String linesCount = Files.readAllLines(path).get(size - 1);
    String line = currentWord
        + " , "
        + result
        + " , "
        + timerLabel.getText()
        + " , "
        + Integer.toString(maximumTime);
    FileWriter fileWriter;

    /*
     * Write the line to the file. If an IOException is raised, then
     * print out an error message to the console
     */
    try {
      fileWriter = new FileWriter("DATABASE/" + linesCount, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(line);
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }

  /**
   * This method runs after the end of each round and auto-saves the drawing made
   * by the user
   *
   * @throws IOException
   */
  private void autoSaveDrawing() throws IOException {

    final File autoSaveDrawingsFolder = new File("DATABASE/autosaves");

    // If the autosaves folder does not already exist, create it
    if (!autoSaveDrawingsFolder.exists()) {
      autoSaveDrawingsFolder.mkdir();
    }

    // Create a new png file of the canvas screenshot and save it to the autosaves
    // folder
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

    // Get the user data
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    // Updates the leaderboard and receive previous leaderboard information
    leaderBoardList.getItems().clear();
    leaderBoardLabel.setVisible(true);
    leaderBoardList.setVisible(true);
    leaderBoardLabel.setText("Top artists");
    ArrayList<Score> allScores;
    allScores = StatisticsManager.getLeaderBoard(currentWord);
    Score currentScore;

    // Iterate through all the scores and add the records to the leaderboard
    for (int i = 0; i < 10; i++) {
      if (i < allScores.size()) {
        currentScore = allScores.get(i);

        // If the recorded time is equal to the maximum time, the game must have been
        // lost
        if (currentScore.getTime() == gameSettings.getTimeLevel() + 1) {
          leaderBoardList.getItems().add(currentScore.getUsername() + "  LOST");
        } else {
          leaderBoardList
              .getItems()
              .add(currentScore.getUsername() + "  " + currentScore.getTime() + " s");
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
    resetArrow();
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

  private void arrowMotionControlUp() {
    movementDown.jumpTo(Duration.seconds(0));
    movementDown.stop();
    movementUp.setDuration(Duration.seconds(3));
    movementUp.setNode(arrowUp);
    movementUp.setToY(-100);
    movementUp.setAutoReverse(false);
    movementUp.setCycleCount(15);
    movementUp.play();
  }

  private void arrowMotionControlDown() {
    movementUp.jumpTo(Duration.seconds(0));
    movementUp.stop();
    movementDown.setDuration(Duration.seconds(3));
    movementDown.setNode(arrowDown);
    movementDown.setToY(100);
    movementDown.setAutoReverse(false);
    movementDown.setCycleCount(15);
    movementDown.play();
  }

  private void resetArrow() {
    movementUp.jumpTo(Duration.seconds(0));
    movementUp.stop();
    movementDown.jumpTo(Duration.seconds(0));
    movementDown.stop();
  }

  private boolean insideTopPrediction() {

    // Get the uesr data and then get the current words difficulty
    Stage stage = (Stage) root.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    int accuracyLevel = (int) gameSettings.getAccuracyLevel();

    switch (accuracyLevel) {
      case 0:

        for (int i = 3; i < 10; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)) {
            return true;
          }
        }
        return false;
    }

    switch (accuracyLevel) {
      case 1:

        for (int i = 2; i < 10; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)) {
            return true;
          }
        }
        return false;
    }

    switch (accuracyLevel) {
      case 0:

        for (int i = 1; i < 10; i++) {
          if (!isCanvasBlank()
              && modelResultsPieChart
                  .getData()
                  .get(i)
                  .getName()
                  .substring(0, modelResultsPieChart.getData().get(i).getName().indexOf(":"))
                  .equals(currentWord)) {
            return true;
          }
        }
        return false;
    }
    return false;
  }
}
