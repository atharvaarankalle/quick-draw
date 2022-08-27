package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class HowToPlayController {
  /**
   * This method is invoked when the user clicks the "New Game" button. It loads and shows the
   * "Canvas" scene
   *
   * @param event The event that triggered this method.
   */
  @FXML
  private void onStartNewGame(ActionEvent event) {

    // Switch to the "Canvas" scene.
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  /**
   * This method is invoked when the user clicks the "Back to Main Menu" button. It loads and shows
   * the "Main Menu" scene
   *
   * @param event The event that triggered this method.
   */
  @FXML
  private void onBackToMainMenu(ActionEvent event) {

    // Switch to the "Main Menu" scene.
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN_MENU));
  }

  /**
   * This method is invoked when the user clicks the "Exit To Desktop" button. It exits the
   * application.
   *
   * @param event The event that triggered this method.
   */
  @FXML
  private void onExit(ActionEvent event) {
    System.exit(0);
  }
}
