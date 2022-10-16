package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.SoundsManager.BackgroundMusic;
import nz.ac.auckland.se206.controllers.SoundsManager.SoundEffects;

public class MainPanelController implements Initializable {

  /**
   * This method loads an FXML file based on an input string, the file name
   *
   * @param fxml The name of the FXML file to load
   * @return Parent the root node of the FXML file
   * @throws IOException if the FXML file cannot be loaded
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  @FXML private Button gameButton;
  @FXML private BorderPane currentScene;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @param location The location used to resolve relative paths for the root object, or null if the
   *     location is not known.
   * @param resources The resources used to localize the root object, or null if the root object was
   *     not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    HiddenWordFunctions.leaveHiddenMode();
  }

  /**
   * This method is called when the user clicks the "Home" button. It switches the scene to the home
   * scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onHome(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);
    // Set the scene to the home scene
    Parent view = loadFxml("homepage");
    currentScene.setCenter(view);

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  /**
   * This method is called when the user clicks the "Game" button. It switches the scene to the
   * canvas scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onGame(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);
    // Set the scene to the canvas scene and leave hidden mode
    currentScene.setCenter(null);
    HiddenWordFunctions.leaveHiddenMode();
    Parent view = loadFxml("canvas");
    currentScene.setCenter(view);
    SoundsManager.stopWinAndLoseSoundEffects();

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  /**
   * This method is called when the user clicks the "Info" button. It switches the scene to the how
   * to play scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onSwitchToInfo(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);

    // Set the scene to the how to play scene
    Parent view = loadFxml("howtoplay");
    currentScene.setCenter(view);

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  /**
   * This method is called when the user clicks the "Statistics" button. It switches the scene to
   * the stats scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onSwitchToStatistic(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);

    // Set the scene to the stats scene
    currentScene.setCenter(null);
    Parent view = loadFxml("scoreboard");
    currentScene.setCenter(view);

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  /**
   * This method is called when the user clicks the "Settings" button. It switches the scene to the
   * game settings scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onGameSettings(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);

    // Set the scene to the game settings scene
    Parent view = loadFxml("gamesettings");
    currentScene.setCenter(view);

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  @FXML
  private void onSoundSettings(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);

    // Set the scene to the sound settings scene
    Parent view = loadFxml("soundsettings");
    currentScene.setCenter(view);

    // Stop background music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
  }

  /**
   * This method is called when the user clicks the "Zen Mode" button. It switches the scene to the
   * zen mode scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onSwitchToZenMode(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);

    // Stop the main menu music and play the mainPanel mode music
    SoundsManager.stopBackgroundMusic(BackgroundMusic.HIDDEN);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
    SoundsManager.stopWinAndLoseSoundEffects();
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);
    // Set the scene to the zen mode scene
    currentScene.setCenter(null);
    Parent view = loadFxml("zenmode");
    currentScene.setCenter(view);
  }

  /**
   * This method is called when the user clicks the "Hidden Mode" button. It switches the scene to
   * the hidden mode scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onHiddenMode(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);

    // Stop the music for all modes and play the hidden mode music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);
    // Set the scene to the hidden mode scene and enter hidden mode
    currentScene.setCenter(null);
    HiddenWordFunctions.toHiddenMode();
    Parent view = loadFxml("canvas");
    currentScene.setCenter(view);
  }

  /**
   * This method is called when the user clicks the "Badges" button. It switches the scene to the
   * badges scene on the same stage
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onBadgesClicked(ActionEvent event) throws IOException {
    SoundsManager.playSoundEffects(SoundEffects.BUTTON1);

    // Stop the music for all modes and play the main menu music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopBackgroundMusic(BackgroundMusic.INGAME);
    SoundsManager.stopBackgroundMusic(BackgroundMusic.ZEN);
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);
    // Set the scene to the badges scene
    currentScene.setCenter(null);
    Parent view = loadFxml("badges");
    currentScene.setCenter(view);
  }

  /**
   * This method is invoked when the user clicks the "Back to Main Menu" button. It loads and shows
   * the "Main Menu" scene
   *
   * @param event The event that triggered this method.
   * @throws IOException The event Failed to get sources.
   */
  @FXML
  private void onLogOut(ActionEvent event) throws IOException {
    // Update ingame status
    InGameStatusManager.setInGameStatus(false);

    // Stop all the background music
    SoundsManager.stopWinAndLoseSoundEffects();
    SoundsManager.stopAllBackgroundMusic();

    // Check if GUEST exists, if does, then delete the file
    Path path = Paths.get("DATABASE/GUEST");
    Path guestSettingsPath = Paths.get("DATABASE/usersettings/GUEST");

    // Delete the guest file if it exists
    if (Files.exists(path)) {
      Files.delete(path);
    }

    // Delete the guest settings file if it exists
    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }

    // Switch to the "Main Menu" scene.
    Parent view = loadFxml("homepage");
    currentScene.setCenter(view);
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /**
   * This method is called when the user clicks the "Exit" button. It exits the program
   *
   * @param event Retrieving information from the event stage to produce scene
   */
  @FXML
  private void onExit(ActionEvent event) throws IOException {

    // Check if GUEST exists, if does, then delete the file
    Path path = Paths.get("DATABASE/GUEST");
    Path guestSettingsPath = Paths.get("DATABASE/usersettings/GUEST");

    // Delete the guest file if it exists
    if (Files.exists(path)) {
      Files.delete(path);
    }

    // Delete the guest settings file if it exists
    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }

    System.exit(0);
  }
}
