package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.SoundsManager.sfx;

public class ScoreBoardController {
  @FXML private Label userNameLabel;

  @FXML private Label totalGamesLabel;

  @FXML private Label gamesWonLabel;

  @FXML private Label gamesLostLabel;

  @FXML private Label bestRecordWordLabel;

  @FXML private Label bestRecordTimeLabel;

  @FXML private ListView<String> scoreList;

  @FXML private AnchorPane backgroundPane;

  @FXML private Label textLabel1;

  @FXML private Label textLabel2;

  @FXML private ImageView imageView;

  @FXML private Label imageDescriptorLabel;

  @FXML private Pane imagePane;

  private List<String> scoreListSorted = new ArrayList<String>();

  private int imageDisplayed = 0;

  private Settings gameSettings;

  public void initialize() {
    try {
      // First read the current user id
      Path userDataPath = Paths.get("DATABASE/UserDatas.txt");
      long lineNumber = Files.lines(userDataPath).count();
      String currentID = Files.readAllLines(userDataPath).get((int) lineNumber - 1);

      Platform.runLater(
          () -> {
            Stage stage = (Stage) scoreList.getScene().getWindow();

            this.gameSettings = (Settings) stage.getUserData();

            StatisticsManager.setGameStage(stage);

            updateStatistics(currentID);
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Updates all the statistic details of the player won/loss
  private void updateStatistics(String currentID) {
    // Initally starts by taking in which player information/statistics to store
    userNameLabel.setText(currentID + "'s Stats");
    try {
      StatisticsManager.readUserStatistics(currentID);
      totalGamesLabel.setText(String.valueOf(StatisticsManager.getNumberOfGames()));
      List<Score> wonRecords = StatisticsManager.getRecords();
      for (Score record :
          wonRecords) { // Iterate the recorded play, and assign Lost or time remaining

        if (!(record.getWord().equals("Initial Write"))) {
          if (record.getTime() == gameSettings.getTimeLevel() + 1) {
            scoreList.getItems().add(record.getWord() + "  LOST");
          } else {
            scoreList.getItems().add(record.getWord() + "  " + record.getTime() + " seconds ");
          }
        }
      }
      String topWord = StatisticsManager.getTopWord();
      // After the scan, update all information
      gamesWonLabel.setText(String.valueOf(StatisticsManager.getGameWon()));
      gamesLostLabel.setText(String.valueOf(StatisticsManager.getGameLost()));
      if (topWord != null) {
        bestRecordWordLabel.setText(topWord + "!");
        bestRecordTimeLabel.setText(
            String.valueOf(StatisticsManager.getTopScore()) + " seconds to draw one!");
      } else {
        textLabel1.setText("Oops, seems like you haven't won any games yet...");
        textLabel2.setText("But don't give up! Let's try again!");
      }

      ObservableList<String> scoreListItems = scoreList.getItems();
      if (!scoreListItems.isEmpty()) {
        for (String string : scoreListItems) {
          // Only add records that aren't lost to the list to be sorted
          if (!string.split("  ")[1].equals("LOST")) {
            scoreListSorted.add(string);
          }
        }
        if (!scoreListSorted.isEmpty()) {
          Collections.sort(
              scoreListSorted,
              new Comparator<String>() {
                public int compare(String firstString, String secondString) {
                  int firstInteger = Integer.parseInt(firstString.replaceAll("\\D", ""));
                  int secondInteger = Integer.parseInt(secondString.replaceAll("\\D", ""));
                  return Integer.compare(firstInteger, secondInteger);
                }
              });

          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(0).split("[0-9]")[0].strip()
                      + ".png"));

          imageDescriptorLabel.setText(
              "Your best drawing: " + scoreListSorted.get(0).split("[0-9]")[0].strip());
        } else {
          imagePane.setVisible(false);
        }
      } else {
        imagePane.setVisible(false);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onNextImage() {
    //Play sound effect
    SoundsManager.playSFX(sfx.BUTTON1);
    /*
     * Switch between the images displayed on the imageView
     * For each image, ensure it is able to handle the cases where
     * the next image is null (has not been drawn yet) or the cases
     * for looping the slideshow infinitely
     */
    switch (imageDisplayed) {
      case 0:
        // Switch to the next image if and only if the scoreListSorted size is greater
        // than 2
        if (scoreListSorted.size() >= 2) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(1).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 1;
          imageDescriptorLabel.setText(
              "Your second best drawing: " + scoreListSorted.get(1).split("[0-9]")[0].strip());
        }
        break;
      case 1:
        // Switch to the next image if and only if the scoreListSorted size is greater
        // than or equal
        // to 3
        if (scoreListSorted.size() >= 3) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(2).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 2;
          imageDescriptorLabel.setText(
              "Your third best drawing: " + scoreListSorted.get(2).split("[0-9]")[0].strip());
          // Otherwise if the end of the array has been reached and the size is 2, loop
          // back to the
          // start
        } else if (scoreListSorted.size() == 2) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(0).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 0;
          imageDescriptorLabel.setText(
              "Your best drawing: " + scoreListSorted.get(0).split("[0-9]")[0].strip());
        }
        break;
      case 2:
        // Set the image to the first image in the case that the 3rd image is already
        // being
        // displayed
        imageView.setImage(
            new Image(
                "file:DATABASE/autosaves/"
                    + scoreListSorted.get(0).split("[0-9]")[0].strip()
                    + ".png"));
        imageDisplayed = 0;
        imageDescriptorLabel.setText(
            "Your best drawing: " + scoreListSorted.get(0).split("[0-9]")[0].strip());
        break;
    }
  }

  @FXML
  private void onPreviousImage() {
    //Play sound effect
    SoundsManager.playSFX(sfx.BUTTON1);
    /*
     * Switch between the images displayed on the imageView
     * For each image, ensure it is able to handle the cases where
     * the previous image is null (has not been drawn yet) or the cases
     * for looping the slideshow infinitely
     */
    switch (imageDisplayed) {
      case 0:
        // Switch to the third image if and only if the scoreListSorted size is greater
        // than or
        // equal to 3
        if (scoreListSorted.size() >= 3) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(2).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 2;
          imageDescriptorLabel.setText(
              "Your third best drawing: " + scoreListSorted.get(2).split("[0-9]")[0].strip());
          // Switch to the second image if and only if the scoreListSorted size is equal
          // to 2
        } else if (scoreListSorted.size() == 2) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(1).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 1;
          imageDescriptorLabel.setText(
              "Your second best drawing: " + scoreListSorted.get(1).split("[0-9]")[0].strip());
        }
        break;
      case 1:
        // Set the image to the first image in the case that the second image is already
        // being
        // displayed
        imageView.setImage(
            new Image(
                "file:DATABASE/autosaves/"
                    + scoreListSorted.get(0).split("[0-9]")[0].strip()
                    + ".png"));
        imageDisplayed = 0;
        imageDescriptorLabel.setText(
            "Your best drawing: " + scoreListSorted.get(0).split("[0-9]")[0].strip());
        break;
        // Set the image to the second image in the case that the third image is already
        // being
        // displayed
      case 2:
        imageView.setImage(
            new Image(
                "file:DATABASE/autosaves/"
                    + scoreListSorted.get(1).split("[0-9]")[0].strip()
                    + ".png"));
        imageDisplayed = 1;
        imageDescriptorLabel.setText(
            "Your second best drawing: " + scoreListSorted.get(1).split("[0-9]")[0].strip());
        break;
    }
  }
}
