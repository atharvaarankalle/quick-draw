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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    int num = 0;

    // Process in which, UserData information being received
    String line = email_textfield.getText();
    Path path = Paths.get("UserDatas.txt");
    long count = Files.lines(path).count();

    /// Read each line
    for (int i = 0; i < count; i++) {
      String vertification = Files.readAllLines(path).get(i);
      if (vertification.equals(line)) { // Confirmation of valid user
        addLine();
        Alert msg = new Alert(AlertType.CONFIRMATION);
        num += 1;
        msg.setTitle(email_textfield.getText());
        msg.setContentText("Username and password matched");
        msg.showAndWait();
        Scene currentScene = ((Button) event.getSource()).getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
        break;
      }
    }

    if (num == 0) { // Error showing mismatch
      Alert msg = new Alert(AlertType.ERROR);
      msg.setTitle(email_textfield.getText());
      msg.setContentText("No such Username : " + email_textfield.getText());
      msg.showAndWait();
    }
  }

  @FXML
  private void signupButton(ActionEvent event) throws IOException {

    int num = 0;

    // Process in which, UserData information being received
    String line = email_textfield.getText();
    Path path = Paths.get("UserDatas.txt");
    long count = Files.lines(path).count();

    /// Read each line
    for (int i = 0; i < count; i++) {
      String redundant = Files.readAllLines(path).get(i);
      if (redundant.equals(line)) { // Confirmation of valid user
        num++;
        Alert msg = new Alert(AlertType.ERROR);
        msg.setTitle(email_textfield.getText());
        msg.setContentText("Existing username, please login");
        msg.showAndWait();
        break;
      }
    }

    if (num == 0) {
      Alert msg = new Alert(AlertType.CONFIRMATION);
      msg.setTitle(email_textfield.getText());
      msg.setContentText("Username and password matched");
      msg.showAndWait();
      addLine();
      Scene currentScene = ((Button) event.getSource()).getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
    }
  }

  private void addLine() throws IOException {

    String line = email_textfield.getText();
    FileWriter file_writer;

    try {
      file_writer = new FileWriter("UserDatas.txt", true);
      BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
      buffered_Writer.write(line);
      buffered_Writer.newLine();
      buffered_Writer.flush();
      buffered_Writer.close();

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }
}
