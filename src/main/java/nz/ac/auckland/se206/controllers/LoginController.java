package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

// Author : Ash, Nov 2, 2018 at 22:58, StackOverflow

public class LoginController {

  @FXML
  private void loginButton(ActionEvent event) {
    // Switch to the "Canvas" scene.
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  @FXML
  private void signupButton(ActionEvent event) {
    // Switch to the "Canvas" scene.
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }
}
