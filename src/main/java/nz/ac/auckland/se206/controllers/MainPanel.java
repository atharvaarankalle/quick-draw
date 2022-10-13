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
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

public class MainPanel implements Initializable {

  @FXML
  private BorderPane CurrentScene;

  @FXML
  private Button GameButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  private void onHome(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
   // enableButtons();
    //homeButton.setDisable(true);
  }

  @FXML
  private void onGame(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    CurrentScene.setCenter(null);
    Parent view = loadFxml("canvas");
    CurrentScene.setCenter(view);
    //enableButtons();
    //gameButton.setDisable(true);
  }

  @FXML
  private void onInfo(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("howtoplay");
    CurrentScene.setCenter(view);
    //enableButtons();
    //infoButton.setDisable(true);
  }

  @FXML
  private void onStatistic(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    CurrentScene.setCenter(null);
    Parent view = loadFxml("scoreboard");
    CurrentScene.setCenter(view);
    //enableButtons();
   // statsButton.setDisable(true);
  }

  @FXML
  private void onGameSettings(ActionEvent event) throws IOException {
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("gamesettings");
    CurrentScene.setCenter(view);
   // enableButtons();
    //settingsButton.setDisable(true);
  }

  @FXML
  private void onSoundSettings(ActionEvent event) throws IOException{
    SoundsManager.playSFX(sfx.BUTTON1);
    Parent view = loadFxml("soundsettings");
    CurrentScene.setCenter(view);
    //enableButtons();
    //musicButton.setDisable(true);
  }

  @FXML
  private void onZenMode(ActionEvent event) throws IOException {
    CurrentScene.setCenter(null);
    Parent view = loadFxml("zenmode");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  /**
   * This method is invoked when the user clicks the "Back to Main Menu" button.
   * It loads and shows
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
    if (Files.exists(path) || Files.exists(guestSettingsPath)) {
      Files.delete(path);
      Files.delete(guestSettingsPath);
      System.exit(0);
    }
    
    // Switch to the "Main Menu" scene.
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
    //enableButtons();
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  @FXML
  private void onExit(ActionEvent event) throws IOException {
    System.exit(0);
  }

  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  // private void enableButtons(){
  //   gameButton.setDisable(false);
  //   homeButton.setDisable(false);
  //   statsButton.setDisable(false);
  //   infoButton.setDisable(false);
  //   musicButton.setDisable(false);
  //   settingsButton.setDisable(false);
  // }
}
