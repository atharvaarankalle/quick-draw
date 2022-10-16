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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.SoundsManager.BackgroundMusic;
import nz.ac.auckland.se206.controllers.SoundsManager.SoundEffects;

// Author : Ash, Nov 2, 2018 at 22:58, StackOverflow
// https://stackoverflow.com/questions/53020451/how-to-create-javafx-save-read-information-from-text-file-and-letting-user-to-e

public class LoginController implements Initializable {

  @FXML private AnchorPane loginRoot;

  @FXML private TextField emailTextField;

  @FXML private ListView<String> usersListView;

  @FXML private ObservableList<String> usersList = FXCollections.observableArrayList();

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a login page
   * and brings users details
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Initally initalize the whole login page
    emailTextField.getText();
    usersListView.setItems(usersList);
    usersListView.setCellFactory(
        new Callback<
            ListView<String>, ListCell<String>>() { // Process for calling LoginInformation class
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

        // If the user is not already in the list, add them
        if (!usersList.contains(currentUser)) {
          usersList.add(currentUser);

          // If the user list contains "GUEST" remove it
          if (usersList.contains("GUEST")) {
            usersList.remove("GUEST");
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the sign up button. It will add a new user and
   * automatically log them in
   *
   * @param event Brings the information from the stage to display different scene event
   */
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

        // Update the user of the status of the log in
        msg.setTitle("Error creating user");
        msg.setHeaderText("Error Creating User");
        msg.setContentText("The username you entered is either blank or already exists!");
        msg.showAndWait();
        break;
      }
    }

    // Add the user to the UserDatas.txt file and log them in
    if (num == 0 && !line.isBlank()) { // Process registeration
      Alert msg = new Alert(AlertType.INFORMATION);
      msg.setTitle("Sign Up Successful");
      msg.setHeaderText("Sign Up Successful!");
      msg.setContentText("Successfully created new user with username: " + line);
      msg.getButtonTypes().clear();
      msg.getButtonTypes().addAll(ButtonType.OK);
      SoundsManager.playSoundEffects(SoundEffects.LOGIN);
      msg.showAndWait();
      addLine(line);

      // Change the scene to the main scene
      SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
      Scene currentScene = ((Button) event.getSource()).getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
      usersList.add(emailTextField.getText());
      emailTextField.clear();
    }
  }

  /**
   * This method is called when the user selects their username from the ListView. It will log the
   * user in
   *
   * @param event Brings the information from the stage to display different scene event
   */
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
        if (updateVolumeStatus(userName) == 1) {
          SoundsManager.setMuteAllBackgroundMusic(true);
          SoundsManager.setMuteAllSoundEffects(true);
        } else {
          SoundsManager.setMuteAllBackgroundMusic(false);
          SoundsManager.setMuteAllSoundEffects(false);
        }
        SoundsManager.playSoundEffects(SoundEffects.LOGIN);
        // msg.showAndWait();

        // Change the scene to the main scene
        SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
        Scene currentScene = ((ListView) event.getSource()).getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
        break;
      }
    }

    usersListView.getSelectionModel().clearSelection();
  }

  /**
   * This method is called to add a line to the UserDatas.txt file
   *
   * @param event Brings the information from the stage to display different scene event
   */
  private void addLine(String line) throws IOException {

    // Get the user data
    Stage stage = (Stage) loginRoot.getScene().getWindow();

    Settings gameSettings = (Settings) stage.getUserData();

    FileWriter fileWriter;

    FileWriter fileWriterUserFile;

    boolean userFileExists = new File("DATABASE/usersettings/" + line).isFile();

    /*
     * Write the line to the file. If an IOException is raised, then
     * print out an error message to the console
     */
    try {
      fileWriter = new FileWriter("DATABASE/UserDatas.txt", true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(line);
      bufferedWriter.newLine();
      bufferedWriter.flush();
      bufferedWriter.close();

      final File userSettingsFolder = new File("DATABASE/usersettings");

      // Create the folder if it doesn't exist
      if (!userSettingsFolder.exists()) {
        userSettingsFolder.mkdir();
      }

      // Create the user file if it doesn't exist
      if (!userFileExists) {
        fileWriterUserFile = new FileWriter("DATABASE/usersettings/" + line, true);
        BufferedWriter bufferedWriterUserFile = new BufferedWriter(fileWriterUserFile);
        bufferedWriterUserFile.write("0.0 , 0.0 , 0.0 , 0.0 , 50.0 , 50.0 , 0");
        bufferedWriterUserFile.newLine();
        bufferedWriterUserFile.flush();
        bufferedWriterUserFile.close();
      }

      gameSettings.setCurrentUser(line);

    } catch (IOException e) {
      System.out.println("Add line failed!!" + e);
    }
  }

  /**
   * This method is called when the user clicks the guest button. It will log the user in as a guest
   *
   * @param event Brings the information from the stage to display different scene event
   */
  @FXML
  private void onSwitchToGuestMode(ActionEvent event) throws IOException {

    // Get the user data from the stage and set the current user to guest
    Stage stage = (Stage) loginRoot.getScene().getWindow();
    Settings gameSettings = (Settings) stage.getUserData();
    String line = "GUEST";
    gameSettings.setCurrentUser(line);
    addLine(line);

    // Start playing background BackgroundMusic
    SoundsManager.playBackgroundMusic(BackgroundMusic.MAINPANEL);
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN));
  }

  private int updateVolumeStatus(String userName) throws IOException {
    // Get the user data from the current user file
    Path userStatsPath = Paths.get("DATABASE/usersettings/" + userName);
    List<String> userStats = Files.readAllLines(userStatsPath);

    // Update the volume of the music and sound effects based on the user's settings
    SoundsManager.changeBackgroundMusicVolume(
        Double.valueOf(userStats.get(userStats.size() - 1).split(" , ")[5]) / 100);
    SoundsManager.changeSoundEffectsVolume(
        Double.valueOf(userStats.get(userStats.size() - 1).split(" , ")[4]) / 100);
    return Integer.valueOf(userStats.get(userStats.size() - 1).split(" , ")[6]);
  }
}
