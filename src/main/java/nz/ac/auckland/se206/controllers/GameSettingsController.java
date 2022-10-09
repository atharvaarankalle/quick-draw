package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GameSettingsController implements Initializable {

  @FXML private Pane settingsRoot;

  @FXML private Slider accuracySlider;

  @FXML private Label accuracyLabel;

  @FXML private Slider wordsSlider;

  @FXML private Label wordsLabel;

  @FXML private Slider timeSlider;

  @FXML private Label timeLabel;

  @FXML private Slider confidenceSlider;

  @FXML private Label confidenceLabel;

  @FXML private Tooltip accuracyTooltip;

  @FXML private Tooltip wordsTooltip;

  @FXML private Tooltip timeTooltip;

  @FXML private Tooltip confidenceTooltip;

  private static String previousUserID = "";

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    accuracyLabel.setTooltip(accuracyTooltip);
    wordsLabel.setTooltip(wordsTooltip);
    timeLabel.setTooltip(timeTooltip);
    confidenceLabel.setTooltip(confidenceTooltip);
    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/18447963/javafx-slider-text-as-tick-label
     */
    accuracySlider.setLabelFormatter(
        new StringConverter<Double>() {
          @Override
          public String toString(Double sliderValue) {
            if (sliderValue < 0.5) {
              return "Easy";
            }

            if (sliderValue < 1.5) {
              return "Medium";
            }

            return "Hard";
          }

          @Override
          public Double fromString(String string) {
            switch (string) {
              case "Easy":
                return 1d;
              case "Medium":
                return 2d;
              case "Hard":
                return 3d;
              default:
                return 1d;
            }
          }
        });

    wordsSlider.setLabelFormatter(
        new StringConverter<Double>() {
          @Override
          public String toString(Double sliderValue) {
            if (sliderValue < 0.5) {
              return "Easy";
            }

            if (sliderValue < 1.5) {
              return "Medium";
            }

            if (sliderValue < 2.5) {
              return "Hard";
            }

            return "Master";
          }

          @Override
          public Double fromString(String string) {
            switch (string) {
              case "Easy":
                return 1d;
              case "Medium":
                return 2d;
              case "Hard":
                return 3d;
              case "Master":
                return 4d;
              default:
                return 1d;
            }
          }
        });

    timeSlider.setLabelFormatter(
        new StringConverter<Double>() {
          @Override
          public String toString(Double sliderValue) {
            if (sliderValue < 0.5) {
              return "Easy";
            }

            if (sliderValue < 1.5) {
              return "Medium";
            }

            if (sliderValue < 2.5) {
              return "Hard";
            }

            return "Master";
          }

          @Override
          public Double fromString(String string) {
            switch (string) {
              case "Easy":
                return 1d;
              case "Medium":
                return 2d;
              case "Hard":
                return 3d;
              case "Master":
                return 4d;
              default:
                return 1d;
            }
          }
        });

    confidenceSlider.setLabelFormatter(
        new StringConverter<Double>() {
          @Override
          public String toString(Double sliderValue) {
            if (sliderValue < 0.5) {
              return "Easy";
            }

            if (sliderValue < 1.5) {
              return "Medium";
            }

            if (sliderValue < 2.5) {
              return "Hard";
            }

            return "Master";
          }

          @Override
          public Double fromString(String string) {
            switch (string) {
              case "Easy":
                return 1d;
              case "Medium":
                return 2d;
              case "Hard":
                return 3d;
              case "Master":
                return 4d;
              default:
                return 1d;
            }
          }
        });

    Platform.runLater(
        () -> {
          Stage stage = (Stage) settingsRoot.getScene().getWindow();

          Settings gameSettings = (Settings) stage.getUserData();

          if (!(previousUserID.equals(gameSettings.getCurrentUser()))) {
            previousUserID = gameSettings.getCurrentUser();

            String currentLine;
            String lastLine = "";
            String[] separatedUserInfo = {""};
            try {
              BufferedReader bufferedReader =
                  new BufferedReader(new FileReader("DATABASE/usersettings/" + previousUserID));

              while ((currentLine = bufferedReader.readLine()) != null) {
                lastLine = currentLine;
              }

              bufferedReader.close();

              separatedUserInfo = lastLine.split(" , ");

              gameSettings.setAccuracyLevel(Double.valueOf(separatedUserInfo[0]));
              gameSettings.setWordsLevel(Double.valueOf(separatedUserInfo[1]));
              gameSettings.setTimeLevel(Double.valueOf(separatedUserInfo[2]));
              gameSettings.setConfidenceLevel(Double.valueOf(separatedUserInfo[3]));

              accuracySlider.setValue(Double.valueOf(separatedUserInfo[0]));
              wordsSlider.setValue(Double.valueOf(separatedUserInfo[1]));
              timeSlider.setValue(Double.valueOf(separatedUserInfo[2]));
              confidenceSlider.setValue(Double.valueOf(separatedUserInfo[3]));

            } catch (IOException e) {

            }
          } else {
            accuracySlider.setValue(gameSettings.getAccuracyLevel());
            wordsSlider.setValue(gameSettings.getWordsLevel());
            timeSlider.setValue(gameSettings.getTimeSliderPosition());
            confidenceSlider.setValue(gameSettings.getConfidenceSliderPosition());
          }

          switch ((int) accuracySlider.getValue()) {
            case 0:
              accuracySlider.setStyle("-fx-control-inner-background: green");
              break;
            case 1:
              accuracySlider.setStyle("-fx-control-inner-background: yellow");
              break;
            case 2:
              accuracySlider.setStyle("-fx-control-inner-background: red");
              break;
            default:
              accuracySlider.setStyle("-fx-control-inner-background: green");
          }

          switch ((int) wordsSlider.getValue()) {
            case 0:
              wordsSlider.setStyle("-fx-control-inner-background: green");
              break;
            case 1:
              wordsSlider.setStyle("-fx-control-inner-background: yellow");
              break;
            case 2:
              wordsSlider.setStyle("-fx-control-inner-background: orange");
              break;
            case 3:
              wordsSlider.setStyle("-fx-control-inner-background: red");
              break;
            default:
              wordsSlider.setStyle("-fx-control-inner-background: green");
          }

          switch ((int) timeSlider.getValue()) {
            case 0:
              timeSlider.setStyle("-fx-control-inner-background: green");
              break;
            case 1:
              timeSlider.setStyle("-fx-control-inner-background: yellow");
              break;
            case 2:
              timeSlider.setStyle("-fx-control-inner-background: orange");
              break;
            case 3:
              timeSlider.setStyle("-fx-control-inner-background: red");
              break;
            default:
              timeSlider.setStyle("-fx-control-inner-background: green");
          }

          switch ((int) confidenceSlider.getValue()) {
            case 0:
              confidenceSlider.setStyle("-fx-control-inner-background: green");
              break;
            case 1:
              confidenceSlider.setStyle("-fx-control-inner-background: yellow");
              break;
            case 2:
              confidenceSlider.setStyle("-fx-control-inner-background: orange");
              break;
            case 3:
              confidenceSlider.setStyle("-fx-control-inner-background: red");
              break;
            default:
              confidenceSlider.setStyle("-fx-control-inner-background: green");
          }
        });
  }

  @FXML
  private void onAccuracyDragDetected() throws IOException {

    FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserID, true);

    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/29637688/javafx-slider-event-listener-being-called-before-it-snaps-to-the-nearest-tick
     */
    accuracySlider
        .valueProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null
                  && !newValue.equals(oldValue)
                  && !accuracySlider.isValueChanging()) {

                Stage stage = (Stage) accuracyLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setAccuracyLevel(newValue.doubleValue());

                stage.setUserData(gameSettings);

                try {
                  String line =
                      newValue.toString()
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                } catch (IOException e) {

                }

                switch (newValue.intValue()) {
                  case 0:
                    accuracySlider.setStyle("-fx-control-inner-background: green");
                    break;
                  case 1:
                    accuracySlider.setStyle("-fx-control-inner-background: yellow");
                    break;
                  case 2:
                    accuracySlider.setStyle("-fx-control-inner-background: red");
                    break;
                }
              }
            });

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/51089812/javafx-slider-not-invoking-valuepropertys-changelistener-for-min-and-max-values
     */
    accuracySlider
        .valueChangingProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              double sliderValue = accuracySlider.getValue();
              boolean stoppedUpdating = oldValue && !newValue;
              boolean isSliderValueAtMinOrMax =
                  sliderValue == accuracySlider.getMin() || sliderValue == accuracySlider.getMax();

              if (stoppedUpdating && isSliderValueAtMinOrMax) {

                Stage stage = (Stage) accuracyLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setAccuracyLevel(accuracySlider.getValue());

                stage.setUserData(gameSettings);

                double sliderFinalValue = 0.0;

                if (sliderValue == accuracySlider.getMax()) {
                  sliderFinalValue = accuracySlider.getMax();
                } else if (sliderValue == accuracySlider.getMin()) {
                  sliderFinalValue = accuracySlider.getMin();
                }

                try {
                  String line =
                      Double.toString(sliderFinalValue)
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                if (sliderValue == accuracySlider.getMin()) {
                  accuracySlider.setStyle("-fx-control-inner-background: green");
                } else if (sliderValue == accuracySlider.getMax()) {
                  accuracySlider.setStyle("-fx-control-inner-background: red");
                }
              }
            });
  }

  @FXML
  private void onWordsDragDetected() throws IOException {

    FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserID, true);

    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/29637688/javafx-slider-event-listener-being-called-before-it-snaps-to-the-nearest-tick
     */
    wordsSlider
        .valueProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null
                  && !newValue.equals(oldValue)
                  && !wordsSlider.isValueChanging()) {

                Stage stage = (Stage) wordsLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setWordsLevel(newValue.doubleValue());

                stage.setUserData(gameSettings);

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + newValue.toString()
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                switch (newValue.intValue()) {
                  case 0:
                    wordsSlider.setStyle("-fx-control-inner-background: green");
                    break;
                  case 1:
                    wordsSlider.setStyle("-fx-control-inner-background: yellow");
                    break;
                  case 2:
                    wordsSlider.setStyle("-fx-control-inner-background: orange");
                    break;
                  case 3:
                    wordsSlider.setStyle("-fx-control-inner-background: red");
                    break;
                }
              }
            });

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/51089812/javafx-slider-not-invoking-valuepropertys-changelistener-for-min-and-max-values
     */
    wordsSlider
        .valueChangingProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              double sliderValue = wordsSlider.getValue();
              boolean stoppedUpdating = oldValue && !newValue;
              boolean isSliderValueAtMinOrMax =
                  sliderValue == wordsSlider.getMin() || sliderValue == wordsSlider.getMax();

              if (stoppedUpdating && isSliderValueAtMinOrMax) {

                Stage stage = (Stage) wordsLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setWordsLevel(wordsSlider.getValue());

                stage.setUserData(gameSettings);

                double sliderFinalValue = 0.0;

                if (sliderValue == wordsSlider.getMax()) {
                  sliderFinalValue = wordsSlider.getMax();
                } else if (sliderValue == wordsSlider.getMin()) {
                  sliderFinalValue = wordsSlider.getMin();
                }

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + Double.toString(sliderFinalValue)
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                if (sliderValue == wordsSlider.getMin()) {
                  wordsSlider.setStyle("-fx-control-inner-background: green");
                } else if (sliderValue == wordsSlider.getMax()) {
                  wordsSlider.setStyle("-fx-control-inner-background: red");
                }
              }
            });
  }

  @FXML
  private void onTimeDragDetected() throws IOException {

    FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserID, true);

    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/29637688/javafx-slider-event-listener-being-called-before-it-snaps-to-the-nearest-tick
     */
    timeSlider
        .valueProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null && !newValue.equals(oldValue) && !timeSlider.isValueChanging()) {

                Stage stage = (Stage) timeLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setTimeLevel(newValue.doubleValue());

                stage.setUserData(gameSettings);

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + newValue.toString()
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                switch (newValue.intValue()) {
                  case 0:
                    timeSlider.setStyle("-fx-control-inner-background: green");
                    break;
                  case 1:
                    timeSlider.setStyle("-fx-control-inner-background: yellow");
                    break;
                  case 2:
                    timeSlider.setStyle("-fx-control-inner-background: orange");
                    break;
                  case 3:
                    timeSlider.setStyle("-fx-control-inner-background: red");
                    break;
                }
              }
            });

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/51089812/javafx-slider-not-invoking-valuepropertys-changelistener-for-min-and-max-values
     */
    timeSlider
        .valueChangingProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              double sliderValue = timeSlider.getValue();
              boolean stoppedUpdating = oldValue && !newValue;
              boolean isSliderValueAtMinOrMax =
                  sliderValue == timeSlider.getMin() || sliderValue == timeSlider.getMax();

              if (stoppedUpdating && isSliderValueAtMinOrMax) {

                Stage stage = (Stage) timeLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setTimeLevel(timeSlider.getValue());

                stage.setUserData(gameSettings);

                double sliderFinalValue = 0.0;

                if (sliderValue == timeSlider.getMax()) {
                  sliderFinalValue = timeSlider.getMax();
                } else if (sliderValue == timeSlider.getMin()) {
                  sliderFinalValue = timeSlider.getMin();
                }

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + Double.toString(sliderFinalValue)
                          + " , "
                          + Double.toString(confidenceSlider.getValue());
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                if (sliderValue == timeSlider.getMin()) {
                  timeSlider.setStyle("-fx-control-inner-background: green");
                } else if (sliderValue == timeSlider.getMax()) {
                  timeSlider.setStyle("-fx-control-inner-background: red");
                }
              }
            });
  }

  @FXML
  private void onConfidenceDragDetected() throws IOException {

    FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserID, true);

    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/29637688/javafx-slider-event-listener-being-called-before-it-snaps-to-the-nearest-tick
     */
    confidenceSlider
        .valueProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != null
                  && !newValue.equals(oldValue)
                  && !confidenceSlider.isValueChanging()) {

                Stage stage = (Stage) confidenceLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setConfidenceLevel(newValue.doubleValue());

                stage.setUserData(gameSettings);

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + newValue.toString();
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                switch (newValue.intValue()) {
                  case 0:
                    confidenceSlider.setStyle("-fx-control-inner-background: green");
                    break;
                  case 1:
                    confidenceSlider.setStyle("-fx-control-inner-background: yellow");
                    break;
                  case 2:
                    confidenceSlider.setStyle("-fx-control-inner-background: orange");
                    break;
                  case 3:
                    confidenceSlider.setStyle("-fx-control-inner-background: red");
                    break;
                }
              }
            });

    /*
     * Code adapted from:
     * https://stackoverflow.com/questions/51089812/javafx-slider-not-invoking-valuepropertys-changelistener-for-min-and-max-values
     */
    confidenceSlider
        .valueChangingProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              double sliderValue = confidenceSlider.getValue();
              boolean stoppedUpdating = oldValue && !newValue;
              boolean isSliderValueAtMinOrMax =
                  sliderValue == confidenceSlider.getMin()
                      || sliderValue == confidenceSlider.getMax();

              if (stoppedUpdating && isSliderValueAtMinOrMax) {

                Stage stage = (Stage) confidenceLabel.getScene().getWindow();

                Settings gameSettings = (Settings) stage.getUserData();

                gameSettings.setConfidenceLevel(confidenceSlider.getValue());

                stage.setUserData(gameSettings);

                double sliderFinalValue = 0.0;

                if (sliderValue == confidenceSlider.getMax()) {
                  sliderFinalValue = confidenceSlider.getMax();
                } else if (sliderValue == confidenceSlider.getMin()) {
                  sliderFinalValue = confidenceSlider.getMin();
                }

                try {
                  String line =
                      Double.toString(accuracySlider.getValue())
                          + " , "
                          + Double.toString(wordsSlider.getValue())
                          + " , "
                          + Double.toString(timeSlider.getValue())
                          + " , "
                          + Double.toString(sliderFinalValue);
                  bufferedWriter.write(line);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  bufferedWriter.close();
                  fileWriter.close();
                } catch (IOException e) {

                }

                if (sliderValue == confidenceSlider.getMin()) {
                  confidenceSlider.setStyle("-fx-control-inner-background: green");
                } else if (sliderValue == confidenceSlider.getMax()) {
                  confidenceSlider.setStyle("-fx-control-inner-background: red");
                }
              }
            });
  }
}
