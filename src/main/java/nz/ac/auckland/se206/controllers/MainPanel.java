package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
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

public class MainPanel implements Initializable {

  @FXML private BorderPane CurrentScene;

  @FXML private Button GameButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  private void onHome(ActionEvent event) throws IOException {
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  @FXML
  private void onGame(ActionEvent event) throws IOException {
    CurrentScene.setCenter(null);
    Parent view = loadFxml("canvas");
    CurrentScene.setCenter(view);
    GameButton.setDisable(true);
  }

  @FXML
  private void onInfo(ActionEvent event) throws IOException {
    Parent view = loadFxml("howtoplay");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
  }

  @FXML
  private void onStatistic(ActionEvent event) throws IOException {
    CurrentScene.setCenter(null);
    Parent view = loadFxml("scoreboard");
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

    // Switch to the "Main Menu" scene.
    Parent view = loadFxml("homepage");
    CurrentScene.setCenter(view);
    GameButton.setDisable(false);
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
}
