package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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

    Platform.runLater(
        () -> {
          Stage stage = (Stage) settingsRoot.getScene().getWindow();

          Settings gameSettings = (Settings) stage.getUserData();

          accuracySlider.setValue(gameSettings.getAccuracyLevel());
          wordsSlider.setValue(gameSettings.getWordsLevel());
          timeSlider.setValue(gameSettings.getTimeSliderPosition());
        });
  }

  @FXML
  private void onAccuracyDragDetected() {

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
              }
            });
  }

  @FXML
  private void onWordsDragDetected() {
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
              }
            });
  }

  @FXML
  private void onTimeDragDetected() {
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
              }
            });
  }
}
