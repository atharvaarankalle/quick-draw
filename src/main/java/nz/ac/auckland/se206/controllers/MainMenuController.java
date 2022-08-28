package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class MainMenuController {
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
   * This method is invoked when the user clicks the "How To Play" button. It loads and shows the
   * "How To Play" scene
   *
   * @param event The event that triggered this method.
   */
  @FXML
  private void onDisplayInstructions(ActionEvent event) {

    // Switch to the "How to Play" scene.
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.HOW_TO_PLAY));
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
