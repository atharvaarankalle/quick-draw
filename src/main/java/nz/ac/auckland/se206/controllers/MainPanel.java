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
import nz.ac.auckland.se206.controllers.SoundsManager.bgm;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

public class MainPanel implements Initializable {

  @FXML private BorderPane CurrentScene;

  @FXML private Button GameButton;

  /**
   * JavaFX calls this method once the GUI elements are loaded.
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    HiddenWordFunctions.leaveHiddenMode();
  }

  /**
   * This method is called when the user clicks the "Home" button. It switches the scene to the home
   * scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onHome(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.playBGM(bgm.MAINPANEL);
   // enableButtons();
    //homeButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks the "Game" button. It switches the scene to the
   * canvas scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onGame(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    CurrentScene.setCenter(null);
    HiddenWordFunctions.leaveHiddenMode();
    Parent view = loadFxml("canvas");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.playBGM(bgm.MAINPANEL);
    //enableButtons();
    //gameButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks the "Info" button. It switches the scene to the how
   * to play scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onInfo(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("howtoplay");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.playBGM(bgm.MAINPANEL);
    //enableButtons();
    //infoButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks the "Statistics" button. It switches the scene to
   * the stats scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onStatistic(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    CurrentScene.setCenter(null);
    Parent view = loadFxml("scoreboard");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.playBGM(bgm.MAINPANEL);
    //enableButtons();
   // statsButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks the "Settings" button. It switches the scene to the
   * game settings scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onGameSettings(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("gamesettings");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.playBGM(bgm.MAINPANEL);
   // enableButtons();
    //settingsButton.setDisable(true);
  }

  @FXML
  private void onSoundSettings(ActionEvent event) throws IOException{
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("soundsettings");
    CurrentScene.setCenter(view);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.playBGM(bgm.MAINPANEL);
    //enableButtons();
    //musicButton.setDisable(true);
  }

  /**
   * This method is called when the user clicks the "Zen Mode" button. It switches the scene to the
   * zen mode scene on the same stage
   *
   * @param event
   */
  @FXML
  private void onZenMode(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    SoundsManager.stopBGM(bgm.HIDDEN);
    SoundsManager.stopBGM(bgm.INGAME);
    CurrentScene.setCenter(null);
    Parent view = loadFxml("zenmode");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  @FXML
  private void onHiddenMode(ActionEvent event) throws IOException{
    SoundsManager.playSFX(sfx.BUTTON1);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.playBGM(bgm.MAINPANEL);
    CurrentScene.setCenter(null);
    HiddenWordFunctions.toHiddenMode();
    Parent view = loadFxml("canvas");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  @FXML
  private void onBadgesClicked(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    SoundsManager.stopBGM(bgm.INGAME);
    SoundsManager.stopBGM(bgm.ZEN);
    SoundsManager.playBGM(bgm.MAINPANEL);
    CurrentScene.setCenter(null);
    Parent view = loadFxml("badges");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  /**
   * This method is invoked when the user clicks the "Back to Main Menu" button. It loads and shows
   * the "Main Menu" scene
   *
   * @param event The event that triggered this method.
   * @throws IOException
   */
  @FXML
  private void onLogOut(ActionEvent event) throws IOException {

    SoundsManager.stopWinAndLoseSFX();
    SoundsManager.stopAllBGM();

    // Check if GUEST exists, if does, then delete the file
    Path path = Paths.get("DATABASE/GUEST");
    Path guestSettingsPath = Paths.get("DATABASE/usersettings/GUEST");
    if (Files.exists(path)) {
      Files.delete(path);
    }

    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }

    // Delete the guest user settings file
    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }
    
    // Switch to the "Main Menu" scene.
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
    //enableButtons();
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /**
   * This method is called when the user clicks the "Exit" button. It exits the program
   *
   * @param event
   */
  @FXML
  private void onExit(ActionEvent event) throws IOException {

    // Check if GUEST exists, if does, then delete the file
    Path path = Paths.get("DATABASE/GUEST");
    Path guestSettingsPath = Paths.get("DATABASE/usersettings/GUEST");
    if (Files.exists(path)) {
      Files.delete(path);
    }

    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }

    System.exit(0);
  }

  /**
   * This method loads an FXML file based on an input string, the file name
   *
   * @param fxml The name of the FXML file to load
   * @return Parent the root node of the FXML file
   * @throws IOException
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }
}
