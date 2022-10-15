package nz.ac.auckland.se206.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BadgesController implements Initializable {

  @FXML private Pane badgesRoot;
  @FXML private ImageView quickDrawerIcon;
  @FXML private ImageView speedDemonIcon;
  @FXML private ImageView speedySketcherIcon;
  @FXML private ImageView heatingUpIcon;
  @FXML private ImageView hereComesTheHeatIcon;
  @FXML private ImageView onFireIcon;
  @FXML private ImageView aBuddingArtistIcon;
  @FXML private ImageView seriousSkillsIcon;
  @FXML private ImageView masterArtistIcon;

  /**
   * This method is called when the Badges window is opened. It sets the tooltips for each badge and
   * calls the updateBadgeStatus method to update the badges.
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    /*
     * Added descriptions for each tooltip. The descriptions are displayed
     * to the user using JavaFX tooltips and explain what the user needs to
     * do to earn each badge.
     */
    Tooltip quickDrawerTooltip = new Tooltip("Win a game in under 30 seconds");
    quickDrawerTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(quickDrawerIcon, quickDrawerTooltip);

    Tooltip speedDemonTooltip = new Tooltip("Win a game in under 15 seconds");
    speedDemonTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(speedDemonIcon, speedDemonTooltip);

    Tooltip speedySketcherTooltip = new Tooltip("Win a game in under 10 seconds");
    speedySketcherTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(speedySketcherIcon, speedySketcherTooltip);

    Tooltip heatingUpTooltip = new Tooltip("Win 3 games in a row");
    heatingUpTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(heatingUpIcon, heatingUpTooltip);

    Tooltip hereComesTheHeatTooltip = new Tooltip("Win 5 games in a row");
    hereComesTheHeatTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(hereComesTheHeatIcon, hereComesTheHeatTooltip);

    Tooltip onFireTooltip = new Tooltip("Win 10 games in a row");
    onFireTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(onFireIcon, onFireTooltip);

    Tooltip aBuddingArtistTooltip = new Tooltip("Win 10 games in total");
    aBuddingArtistTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(aBuddingArtistIcon, aBuddingArtistTooltip);

    Tooltip seriousSkillsTooltip =
        new Tooltip("Win 20 games in total on Medium or Hard time difficulty");
    seriousSkillsTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(seriousSkillsIcon, seriousSkillsTooltip);

    Tooltip masterArtistTooltip = new Tooltip("Win 30 games in total on Master time difficulty");
    masterArtistTooltip.setStyle("-fx-font-size: 15px;");
    Tooltip.install(masterArtistIcon, masterArtistTooltip);

    Platform.runLater(
        () -> {

          // Get the user settings data
          Stage stage = (Stage) badgesRoot.getScene().getWindow();

          Settings gameSettings = (Settings) stage.getUserData();

          // Call the updateBadgeStatus to update the badges
          try {
            updateBadgeStatus(gameSettings);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * This method updates the badges based on the user's current statistics in games played
   *
   * @param gameSettings The user's current settings
   * @throws IOException if the file reader cannot be used to read the user statistics file
   */
  private void updateBadgeStatus(Settings gameSettings) throws IOException {

    // Get the user's current statistics
    Path userFilePath = Paths.get("DATABASE/" + gameSettings.getCurrentUser());
    if (Files.exists(userFilePath)) {
      BufferedReader bufferedReader;

      bufferedReader =
          new BufferedReader(new FileReader("DATABASE/" + gameSettings.getCurrentUser()));

      String line = bufferedReader.readLine();

      // Initialise tracking variables
      String[] separatedLine;
      boolean gameWon = false;
      int consecutiveWins = 0;
      int totalWins = 0;
      int mediumOrHardTimeWins = 0;
      int masterTimeWins = 0;

      // Read each line in the user statistics file and determine if the user has won or lost
      while (line != null) {
        separatedLine = line.split(" , ");
        gameWon = separatedLine[1].equals("WON");

        // Calculate the time taken for each game to be completed
        int timeTaken =
            Integer.valueOf(separatedLine[3]) - Integer.valueOf(separatedLine[2].split(" ")[0]);

        // Award the first three badges based on the time taken to complete any game
        if (timeTaken < 30 && gameWon) {
          quickDrawerIcon.setOpacity(1.0);
        }

        if (timeTaken < 15 && gameWon) {
          speedDemonIcon.setOpacity(1.0);
        }

        if (timeTaken < 10 && gameWon) {
          speedySketcherIcon.setOpacity(1.0);
        }

        // If the game has been won, increment the number of consecutive wins and total wins
        if (gameWon) {
          totalWins++;
          consecutiveWins++;

          /*
           * If a game has been won on medium or hard time difficulty, increment the number of wins
           * on medium or hard time difficulty
           */
          if (Integer.valueOf(separatedLine[3]) == 45 || Integer.valueOf(separatedLine[3]) == 30) {
            mediumOrHardTimeWins++;
          }

          /*
           * If a game has been won on master time difficulty, increment the number of wins on master
           * time difficulty
           */
          if (Integer.valueOf(separatedLine[3]) == 15) {
            masterTimeWins++;
          }
        } else {
          // If the game has been lost, reset the number of consecutive wins
          consecutiveWins = 0;
        }

        // Award the next three badges based on the number of consecutive wins
        if (consecutiveWins >= 3) {
          heatingUpIcon.setOpacity(1.0);
        }

        if (consecutiveWins >= 5) {
          hereComesTheHeatIcon.setOpacity(1.0);
        }

        if (consecutiveWins >= 10) {
          onFireIcon.setOpacity(1.0);
        }

        // Award the next three badges based on the number of total wins and time difficulty
        if (totalWins >= 10) {
          aBuddingArtistIcon.setOpacity(1.0);
        }

        if (mediumOrHardTimeWins >= 20) {
          seriousSkillsIcon.setOpacity(1.0);
        }

        if (masterTimeWins >= 30) {
          masterArtistIcon.setOpacity(1.0);
        }

        line = bufferedReader.readLine();
      }

      bufferedReader.close();
    }
  }
}
