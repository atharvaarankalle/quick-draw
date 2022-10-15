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

  @Override
  public void initialize(URL location, ResourceBundle resources) {

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
          Stage stage = (Stage) badgesRoot.getScene().getWindow();

          Settings gameSettings = (Settings) stage.getUserData();

          try {
            updateBadgeStatus(gameSettings);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  private void updateBadgeStatus(Settings gameSettings) throws IOException {
    Path userFilePath = Paths.get("DATABASE/" + gameSettings.getCurrentUser());
    if (Files.exists(userFilePath)) {
      BufferedReader bufferedReader;

      bufferedReader =
          new BufferedReader(new FileReader("DATABASE/" + gameSettings.getCurrentUser()));

      String line = bufferedReader.readLine();

      String[] separatedLine;

      boolean gameWon = false;

      int consecutiveWins = 0;

      int totalWins = 0;

      int mediumOrHardTimeWins = 0;

      int masterTimeWins = 0;

      while (line != null) {
        separatedLine = line.split(" , ");
        gameWon = separatedLine[1].equals("WON");
        int timeTaken =
            Integer.valueOf(separatedLine[3]) - Integer.valueOf(separatedLine[2].split(" ")[0]);

        if (timeTaken < 30 && gameWon) {
          quickDrawerIcon.setOpacity(1.0);
        }

        if (timeTaken < 15 && gameWon) {
          speedDemonIcon.setOpacity(1.0);
        }

        if (timeTaken < 10 && gameWon) {
          speedySketcherIcon.setOpacity(1.0);
        }

        if (gameWon) {
          totalWins++;
          consecutiveWins++;

          if (Integer.valueOf(separatedLine[3]) == 45 || Integer.valueOf(separatedLine[3]) == 30) {
            mediumOrHardTimeWins++;
          }

          if (Integer.valueOf(separatedLine[3]) == 15) {
            masterTimeWins++;
          }
        } else {
          consecutiveWins = 0;
        }

        if (consecutiveWins >= 3) {
          heatingUpIcon.setOpacity(1.0);
        }

        if (consecutiveWins >= 5) {
          hereComesTheHeatIcon.setOpacity(1.0);
        }

        if (consecutiveWins >= 10) {
          onFireIcon.setOpacity(1.0);
        }

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
