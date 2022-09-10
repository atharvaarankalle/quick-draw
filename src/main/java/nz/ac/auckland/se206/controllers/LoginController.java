package nz.ac.auckland.se206.controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

// Author : Ash, Nov 2, 2018 at 22:58, StackOverflow
// https://stackoverflow.com/questions/53020451/how-to-create-javafx-save-read-information-from-text-file-and-letting-user-to-e

public class LoginController implements Initializable {

  @FXML private TextField email_textfield;

  @FXML private ListView<String> usersListView = new ListView<String>();

  @FXML private ObservableList<String> usersList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    email_textfield.getText();
    usersListView.setItems(usersList);

    // Process in which, UserData information being received
    Path path = Paths.get("UserDatas.txt");
    long count;
    try {
      count = Files.lines(path).count();

      /// Read each line
      for (int i = 0; i < count; i++) {
        String currentUser = Files.readAllLines(path).get(i);

        if (!usersList.contains(currentUser)) {
          usersList.add(currentUser);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onSignUp(ActionEvent event) throws IOException {

    int num = 0;

    // Process in which, UserData information being received
    String line = email_textfield.getText();
    Path path = Paths.get("UserDatas.txt");
    long count = Files.lines(path).count();

    /// Read each line
    for (int i = 0; i < count; i++) {
      String redundant = Files.readAllLines(path).get(i);
      if (redundant.equals(line) || line.isBlank()) { // Cannot process registeration
        num++;
        Alert msg = new Alert(AlertType.ERROR);
        msg.setTitle("Error creating user");
        msg.setHeaderText("Error Creating User");
        msg.setContentText("The username you entered is either blank or already exists!");
        msg.showAndWait();
        break;
      }
    }

    if (num == 0 && !line.isBlank()) { // Process registeration
      Alert msg = new Alert(AlertType.INFORMATION);
      msg.setTitle("Sign Up Successful");
      msg.setHeaderText("Sign Up Successful!");
      msg.setContentText("Successfully created new user with username: " + line);
      msg.getButtonTypes().clear();
      msg.getButtonTypes().addAll(ButtonType.OK);
      msg.showAndWait();
      addLine(line);
      Scene currentScene = ((Button) event.getSource()).getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
      usersList.add(email_textfield.getText());
    }
  }

  @FXML
  private void onUserSelected(MouseEvent event) throws IOException {
    int num = 0;

    // Process in which, UserData information being received
    String userName = usersListView.getSelectionModel().getSelectedItem();
    Path path = Paths.get("UserDatas.txt");
    long count = Files.lines(path).count();

    /// Read each line
    for (int i = 0; i < count; i++) {
      String vertification = Files.readAllLines(path).get(i);
      if (vertification.equals(userName)) { // Confirmation of valid user
        addLine(userName);
        Alert msg = new Alert(AlertType.INFORMATION);
        num += 1;
        msg.setTitle("Log In Successful!");
        msg.setHeaderText("Log In Successful!");
        msg.setContentText("You have successfully logged in as: " + userName);
        msg.showAndWait();
        Scene currentScene = ((ListView) event.getSource()).getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
        break;
      }
    }

    if (num == 0) { // Error showing mismatch
      Alert msg = new Alert(AlertType.ERROR);
      msg.setTitle(usersListView.getSelectionModel().getSelectedItem());
      msg.setContentText(
          "No such Username : " + usersListView.getSelectionModel().getSelectedItem());
      msg.showAndWait();
    }

    usersListView.getSelectionModel().clearSelection();
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

  private void addLine(String line) throws IOException {

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
