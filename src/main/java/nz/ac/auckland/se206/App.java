package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.Settings;
import nz.ac.auckland.se206.controllers.SoundsManager;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  /**
   * This method is called when the app is first run, and is used to setup the app
   *
   * @throws IOException if the file cannot be read
   */
  public static void initalize() throws IOException {
    File storageData = new File("DATABASE"); // Create a folder to store all user info
    storageData.mkdir();
    FileWriter fileWriter;

    // Create a file to store the list of users logged in
    fileWriter = new FileWriter("DATABASE/UserDatas.txt", true);
    try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {}
  }

  /**
   * This method is the entry point to the entire JavaFX application
   *
   * @throws IOException if the file cannot be read
   */
  public static void main(final String[] args) throws IOException {

    // Initialize and launch the app
    initalize();
    launch();

    // Check if GUEST exists, if does, then delete the file after the app is closed
    Path path = Paths.get("DATABASE/GUEST");
    Path guestSettingsPath = Paths.get("DATABASE/usersettings/GUEST");
    if (Files.exists(path)) {
      Files.delete(path);
    }

    if (Files.exists(guestSettingsPath)) {
      Files.delete(guestSettingsPath);
    }

    System.exit(0);
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  private Scene scene;

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   * @throws URISyntaxException If a string cannot be parsed as a URI reference.
   */
  @Override
  public void start(final Stage stage) throws IOException, URISyntaxException {

    // Load the FXML files
    SceneManager.addUi(AppUi.HOME_PAGE, loadFxml("homepage"));
    SceneManager.addUi(AppUi.CANVAS, loadFxml("canvas"));
    SceneManager.addUi(AppUi.HOW_TO_PLAY, loadFxml("howtoplay"));
    SceneManager.addUi(AppUi.LOGIN, loadFxml("login"));
    SceneManager.addUi(AppUi.MAIN, loadFxml("mainpanel"));

    // Initialize all sound effects
    SoundsManager.loadSoundEffects();
    SoundsManager.loadBackgroundMusic();
    Settings gameSettings = new Settings();

    // Set the current scene, set the title of the app and show the stage
    scene = new Scene(SceneManager.getUiRoot(AppUi.LOGIN), 900, 821);
    stage.setScene(scene);
    stage.setTitle("Quick, Draw! SE206 Edition");
    stage.setUserData(gameSettings);
    setSliderPositions(stage);
    stage.show();
  }

  /**
   * This method is used to set the slider positions to the last saved positions
   *
   * @param stage The primary stage of the application.
   * @throws IOException If the file cannot be read
   */
  private void setSliderPositions(Stage stage) {
    Path userDataPath = Paths.get("DATABASE/UserDatas.txt");
    long lineNumber;
    String currentID;
    String currentLine;
    String lastLine = "";
    String[] separatedUserInfo = {""};
    try {
      lineNumber = Files.lines(userDataPath).count();

      // If no user has logged in before, then continue
      if (lineNumber > 0) {
        currentID = Files.readAllLines(userDataPath).get((int) lineNumber - 1);

        // If the current ID does not equal "GUEST", then continue
        if (!currentID.equals("GUEST")) {
          BufferedReader bufferedReader =
              new BufferedReader(new FileReader("DATABASE/usersettings/" + currentID));

          // Read the last line of the file
          while ((currentLine = bufferedReader.readLine()) != null) {
            lastLine = currentLine;
          }

          bufferedReader.close();

          separatedUserInfo = lastLine.split(" , ");

          Settings gameSettings = (Settings) stage.getUserData();

          // Set the slider positions in the game settings
          gameSettings.setAccuracyLevel(Double.parseDouble(separatedUserInfo[0]));
          gameSettings.setWordsLevel(Double.parseDouble(separatedUserInfo[1]));
          gameSettings.setTimeLevel(Double.parseDouble(separatedUserInfo[2]));
          gameSettings.setConfidenceLevel(Double.parseDouble(separatedUserInfo[3]));
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
