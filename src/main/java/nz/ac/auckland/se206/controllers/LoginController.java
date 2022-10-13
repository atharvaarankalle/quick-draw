package nz.ac.auckland.se206.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.SoundsManager.bgm;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

// Author : Ash, Nov 2, 2018 at 22:58, StackOverflow
// https://stackoverflow.com/questions/53020451/how-to-create-javafx-save-read-information-from-text-file-and-letting-user-to-e

public class LoginController implements Initializable {

  @FXML
  private TextField emailTextField;

  @FXML
  private ListView<String> usersListView = new ListView<String>();

  @FXML
  private ObservableList<String> usersList = FXCollections.observableArrayList();

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we
   * create a login page
   * and brings users details
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Initally initalize the whole login page
    emailTextField.getText();
    usersListView.setItems(usersList);
    usersListView.setCellFactory(
        new Callback<ListView<String>, ListCell<String>>() { // Process for calling LoginInformation class
          // to work parallel for login/register/storing datas
          @Override
          public ListCell<String> call(ListView<String> param) {
            return new LoginInformation();
          }
        });
    // Process in which, UserData information being received
    Path path = Paths.get("DATABASE/UserDatas.txt");
    long count;
    try {
      count = Files.lines(path).count();

      /// Read each line
      for (int i = 0; i < count; i++) {
        String currentUser = Files.readAllLines(path).get(i);

        if (!usersList.contains(currentUser)) {
          usersList.add(currentUser);

          if (usersList.contains("GUEST")) {
            usersList.remove("GUEST");
          }
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
    String line = emailTextField.getText();
    Path path = Paths.get("DATABASE/UserDatas.txt");
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
      SoundsManager.playSFX(sfx.LOGIN);
      msg.showAndWait();
      addLine(line);
      SoundsManager.playBGM(bgm.MAINPANEL);
      Scene currentScene = ((Button) event.getSource()).getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
      usersList.add(emailTextField.getText());
      emailTextField.clear();
    }
  }

  @FXML
  private void onUserSelected(MouseEvent event) throws IOException {
    int num = 0;

    // Process in which, UserData information being received
    String userName = usersListView.getSelectionModel().getSelectedItem();
    Path path = Paths.get("DATABASE/UserDatas.txt");
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
        if (checkMuteStatus(userName) == 1) {
          SoundsManager.setMuteAllBGM(true);
          SoundsManager.setMuteAllSFX(true);
        }
        SoundsManager.playSFX(sfx.LOGIN);
        msg.showAndWait();
        SoundsManager.playBGM(bgm.MAINPANEL);
        Scene currentScene = ((ListView) event.getSource()).getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
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

  private void addLine(String line) throws IOException {

    FileWriter fileWriter;

    FileWriter fileWriterUserFile;

    boolean userFileExists = new File("DATABASE/" + line).isFile();

    /*
     * Write the line to the file. If an IOException is raised, then
     * print out an error message to the console
     */
    try {
      fileWriter = new FileWriter("DATABASE/UserDatas.txt", true);
      fileWriterUserFile = new FileWriter("DATABASE/" + line, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(line);
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();

      if (!userFileExists) {
        BufferedWriter bufferedWriterUserFile = new BufferedWriter(fileWriterUserFile);
        bufferedWriterUserFile.write("Initial Write , Initial Write , 0 , 0.0 , 0.0 , 0.0 , 0.0 , 75.0 , 75.0 , 0");
        bufferedWriterUserFile.newLine();
        bufferedWriterUserFile.flush();
        bufferedWriterUserFile.close();
      }

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }

  @FXML
  private void onGuestMode(ActionEvent event) throws IOException {

    String line = "GUEST";
    addLine(line);
    // Start playing background bgm
    SoundsManager.playBGM(bgm.MAINPANEL);
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
  }

  private int checkMuteStatus(String userName) throws IOException {
    Path userStatsPath = Paths.get("DATABASE/" + userName);
    List<String> userStats = Files.readAllLines(userStatsPath);
    return Integer.valueOf(userStats.get(userStats.size() - 1).split(" , ")[9]);
  }
}
