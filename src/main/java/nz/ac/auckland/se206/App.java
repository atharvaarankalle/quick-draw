package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  public static void initalize() throws IOException {
    File storageData = new File("DATABASE"); // Create a folder to store all user info
    storageData.mkdir();
    FileWriter fileWriter;
    fileWriter = new FileWriter("DATABASE/UserDatas.txt", true);
    try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {}
  }

  public static void main(final String[] args) throws IOException {

    initalize();
    launch();

    // Check if GUEST exists, if does, then delete the file
    Path path = Paths.get("DATABASE/GUEST");
    if (Files.exists(path)) {
      Files.delete(path);
      System.exit(0);
    }
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
   */
  @Override
  public void start(final Stage stage) throws IOException {

    // Load the FXML files
    SceneManager.addUi(AppUi.HOME_PAGE, loadFxml("homepage"));
    SceneManager.addUi(AppUi.CANVAS, loadFxml("canvas"));
    SceneManager.addUi(AppUi.HOW_TO_PLAY, loadFxml("howtoplay"));
    SceneManager.addUi(AppUi.LOGIN, loadFxml("login"));
    SceneManager.addUi(AppUi.MAIN, loadFxml("mainpage"));

    Settings gameSettings = new Settings();

    // Set the current scene and show the stage
    scene = new Scene(SceneManager.getUiRoot(AppUi.LOGIN), 900, 630);
    stage.setScene(scene);
    stage.setTitle("Quick, Draw! SE206 Edition");
    stage.setUserData(gameSettings);
    setSliderPositions(stage);
    stage.show();
  }

  private void setSliderPositions(Stage stage) {
    Path userDataPath = Paths.get("DATABASE/UserDatas.txt");
    long lineNumber;
    String currentID;
    String currentLine;
    String lastLine = "";
    String[] separatedUserInfo = {""};
    try {
      lineNumber = Files.lines(userDataPath).count();

      if (lineNumber > 0) {
        currentID = Files.readAllLines(userDataPath).get((int) lineNumber - 1);

        BufferedReader bufferedReader =
            new BufferedReader(new FileReader("DATABASE/usersettings/" + currentID));

        while ((currentLine = bufferedReader.readLine()) != null) {
          lastLine = currentLine;
        }

        bufferedReader.close();

        separatedUserInfo = lastLine.split(" , ");

        Settings gameSettings = (Settings) stage.getUserData();

        gameSettings.setAccuracyLevel(Double.parseDouble(separatedUserInfo[0]));
        gameSettings.setWordsLevel(Double.parseDouble(separatedUserInfo[1]));
        gameSettings.setTimeLevel(Double.parseDouble(separatedUserInfo[2]));
        gameSettings.setConfidenceLevel(Double.parseDouble(separatedUserInfo[3]));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
