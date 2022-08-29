package nz.ac.auckland.se206.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

// Author : Ash, Nov 2, 2018 at 22:58, StackOverflow
// https://stackoverflow.com/questions/53020451/how-to-create-javafx-save-read-information-from-text-file-and-letting-user-to-e

public class LoginController implements Initializable {

  @FXML private TextField email_textfield;

  @FXML private PasswordField password_textfield;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    email_textfield.getText();
    password_textfield.getText();
  }

  @FXML
  private void loginButton(ActionEvent event) throws IOException {
    // Switch to the "Canvas" scene.
    addLine();
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  @FXML
  private void signupButton(ActionEvent event) throws IOException {
    // Switch to the "Canvas" scene.
    addLine();
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  private void addLine() throws IOException {

    String line = email_textfield.getText();
    FileWriter file_writer;

    try {
      file_writer = new FileWriter("UserDatas.txt", true);
      BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
      // Path to text file
      Path path = Paths.get("UserDatas.txt");

      // Counts number of line in text file
      long count = Files.lines(path).count();

      /// read each line
      for (int i = 0; i < count; i++) {
        String redundant = Files.readAllLines(path).get(i);
        if (redundant.equals(line)) {
          line = "";
        }
      }
      buffered_Writer.write(line);
      buffered_Writer.newLine();
      buffered_Writer.flush();
      buffered_Writer.close();

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }
}
