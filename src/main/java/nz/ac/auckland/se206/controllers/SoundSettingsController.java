package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.controllers.SoundsManager.SoundEffects;

public class SoundSettingsController implements Initializable {
  @FXML private Pane soundSettingsRoot;
  @FXML private Slider soundEffectSlider;
  @FXML private Slider backgroundMusicSlider;
  @FXML private ImageView muteImageView;

  @FXML private Button muteButton;

  private Image muteImage;

  private Image unmuteImage;

  private Settings gameSettings;

  private String previousUserName = "";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(
        () -> {
          // Get user data
          Stage stage = (Stage) soundSettingsRoot.getScene().getWindow();

          gameSettings = (Settings) stage.getUserData();
          // If the user has changed since the last opening of the Sounds settings page, reset
          // the current user
          if (!(previousUserName.equals(gameSettings.getCurrentUser()))) {
            previousUserName = gameSettings.getCurrentUser();

            String currentLine;
            String lastLine = "";
            String[] separatedUserInfo;
            try {
              BufferedReader bufferedReader =
                  new BufferedReader(new FileReader("DATABASE/usersettings/" + previousUserName));
              // Get the final line of the user sounds settings
              while ((currentLine = bufferedReader.readLine()) != null) {
                lastLine = currentLine;
              }

              bufferedReader.close();

              separatedUserInfo = lastLine.split(" , ");
              // Update the volume settings
              gameSettings.setSfxVolume(Double.valueOf(separatedUserInfo[4]));
              gameSettings.setBgmVolume(Double.valueOf(separatedUserInfo[5]));
              gameSettings.setMuteStatus(Integer.valueOf(separatedUserInfo[6]));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          // Update the icon for mute button
          try {
            updateMuteImage(gameSettings.getMuteStatus());
          } catch (URISyntaxException e) {
            e.printStackTrace();
          }
          // Set the value of the two volume slider
          soundEffectSlider.setValue(gameSettings.getSfxVolume());
          backgroundMusicSlider.setValue(gameSettings.getBgmVolume());

          // Add listeners to both sliders, which alters the ingame volume while the slider is
          // changed
          soundEffectSlider
              .valueProperty()
              .addListener(
                  new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                      SoundsManager.changeSoundEffectsVolume(soundEffectSlider.getValue() / 100);
                      gameSettings.setSfxVolume(soundEffectSlider.getValue());
                    }
                  });
          backgroundMusicSlider
              .valueProperty()
              .addListener(
                  new InvalidationListener() {

                    @Override
                    public void invalidated(Observable observable) {
                      SoundsManager.changeBackgroundMusicVolume(
                          backgroundMusicSlider.getValue() / 100);
                      gameSettings.setBgmVolume(backgroundMusicSlider.getValue());
                    }
                  });
        });
  }

  /*
   * Code adapted from:
   * https://stackoverflow.com/questions/26552495/javafx-set-slider-value-after-
   * dragging-mouse-button
   */
  @FXML
  private void onSoundEffectsDragDetected() {
    // Add listed to sound effect slider, if the slider is moved to a new position, record new
    // settings value and play sfx
    soundEffectSlider
        .valueChangingProperty()
        .addListener(
            new ChangeListener<Boolean>() {

              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean wasChanging,
                  Boolean isNowChanging) {
                // If the value of slider is not changing, record the new setting value and play sfx
                if (!isNowChanging) {
                  SoundsManager.playSoundEffects(SoundEffects.TAP);
                  try {
                    addSettingsLine();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              }
            });
  }

  /*
   * Code adapted from:
   * https://stackoverflow.com/questions/26552495/javafx-set-slider-value-after-
   * dragging-mouse-button
   */
  @FXML
  private void onBackgroundMusicDragDetected() {
    // Add listed to background music slider, if the slider is moved to a new position, record new
    // settings value and play sfx
    backgroundMusicSlider
        .valueChangingProperty()
        .addListener(
            new ChangeListener<Boolean>() {

              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean wasChanging,
                  Boolean isNowChanging) {
                // If the value of slider is not changing, record the new setting value and play sfx
                if (!isNowChanging) {
                  SoundsManager.playSoundEffects(SoundEffects.TAP);
                  try {
                    addSettingsLine();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              }
            });
  }

  @FXML
  private void onMute() throws URISyntaxException, IOException {
    // If the mute status setting is 0, clicking the mute button should mute the game and change
    // mute status to 1
    if (gameSettings.getMuteStatus() == 0) {
      gameSettings.setMuteStatus(1);
      SoundsManager.setMuteAllBackgroundMusic(true);
      SoundsManager.setMuteAllSoundEffects(true);
      updateMuteImage(gameSettings.getMuteStatus());
      addSettingsLine();
    } else {
      // Other wise if mute status is 1, clicking the mute button should unmute the game and change
      // the mute status to 0
      gameSettings.setMuteStatus(0);
      SoundsManager.setMuteAllBackgroundMusic(false);
      SoundsManager.setMuteAllSoundEffects(false);
      updateMuteImage(gameSettings.getMuteStatus());
      addSettingsLine();
    }
  }

  private void updateMuteImage(int status) throws URISyntaxException {
    muteImage = new Image(App.class.getResource("/images/mute.png").toURI().toString());
    unmuteImage = new Image(App.class.getResource("/images/unmute.png").toURI().toString());
    if (status == 1) {
      muteImageView.setImage(unmuteImage);
      muteButton.setText("UNMUTE");
    } else {
      muteImageView.setImage(muteImage);
      muteButton.setText("MUTE");
    }
  }

  private void addSettingsLine() throws IOException {
    // Initialize file writer
    FileWriter fileWriter = new FileWriter("DATABASE/usersettings/" + previousUserName, true);

    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    // Add a line recording all the current user settings value
    try {
      String line =
          Double.toString(gameSettings.getAccuracyLevel())
              + " , "
              + Double.toString(gameSettings.getWordsLevel())
              + " , "
              + Double.toString(gameSettings.getTimeSliderPosition())
              + " , "
              + Double.toString(gameSettings.getConfidenceSliderPosition())
              + " , "
              + Double.toString(soundEffectSlider.getValue())
              + " , "
              + Double.toString(backgroundMusicSlider.getValue())
              + " , "
              + Integer.toString(gameSettings.getMuteStatus());
      bufferedWriter.write(line);
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();
    } catch (IOException e) {
      System.out.println("Add line failed");
    }
  }
}
