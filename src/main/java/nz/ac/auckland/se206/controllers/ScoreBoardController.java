package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class ScoreBoardController {
  @FXML
  private Label userIDLable;

  @FXML
  private Label totalGamesLable;

  @FXML
  private Label gamesWonLable;

  @FXML
  private Label gamesLostLable;

  @FXML
  private Label bestRecordWordLabel;

  @FXML
  private Label bestRecordTimeLabel;

  @FXML
  private Button menuButton;

  @FXML
  private Button toGameButton;

  @FXML
  private Label noStatsLabel;

  @FXML
  private ListView<String> scoreList;

  @FXML
  private AnchorPane backgroundPane;

  @FXML
  private Label textLabel1;

  @FXML
  private Label textLabel2;

  @FXML
  private ImageView imageView;

  @FXML
  private Label imageDescriptorLabel;

  @FXML
  private Pane imagePane;

  private List<String> scoreListSorted = new ArrayList<String>();

  private int imageDisplayed = 0;

  public void initialize() {
    try {
      noStatsLabel.setVisible(false);
      // First read the current user id
      Path userDataPath = Paths.get("DATABASE/UserDatas.txt");
      long lineNumber = Files.lines(userDataPath).count();
      String currentID = Files.readAllLines(userDataPath).get((int) lineNumber - 1);
      updateStats(currentID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onBackToMenu(ActionEvent event) {
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.MAIN_MENU));
  }

  @FXML
  private void onToGame(ActionEvent event) {
    Scene currentScene = ((Button) event.getSource()).getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  private void updateStats(String currentID) {
    userIDLable.setText(currentID + "'s stats");
    try {
      statsManager.readUserStats(currentID);
      statsManager.manageStats();
      totalGamesLable.setText(String.valueOf(statsManager.getNumberOfGames()));
      Map<String,Integer> wonRecords = statsManager.getWordAndRecord();
      Set<String>words = wonRecords.keySet();
      for(String word : words){
      scoreList.getItems().add(word + "  " + wonRecords.get(word) + " seconds");
      }
      String topWord = statsManager.getTopWord();
      // After the scan, update all information
      gamesWonLable.setText(String.valueOf(statsManager.getGameWon()));
      gamesLostLable.setText(String.valueOf(statsManager.getGameLost()));
      if (topWord != null) {
        bestRecordWordLabel.setText(topWord + "!");
        bestRecordTimeLabel.setText(String.valueOf(statsManager.getTopScore()) + " seconds to draw one!");
      } else {
        textLabel1.setText("Oops, seems like you haven't won any games yet...");
        textLabel2.setText("But don't give up! Let's try again!");
      }

      ObservableList<String> scoreListItems = scoreList.getItems();
      if (!scoreListItems.isEmpty()) {
        for (String string : scoreListItems) {
          scoreListSorted.add(string);
        }

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
      } else{
        imagePane.setVisible(false);
      }
    } catch (IOException e) {
      ObservableList<Node> allNodes = backgroundPane.getChildren();
      for (Node node : allNodes) {
        node.setVisible(false);
      }
      noStatsLabel.setVisible(true);
      menuButton.setVisible(true);
      toGameButton.setVisible(true);
    }

  }

  @FXML
  private void onNextImage() {
    switch (imageDisplayed) {
      case 0:
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
        if (scoreListSorted.size() >= 3) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(2).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 2;
          imageDescriptorLabel.setText(
              "Your third best drawing: " + scoreListSorted.get(2).split("[0-9]")[0].strip());
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
    switch (imageDisplayed) {
      case 0:
        if (scoreListSorted.size() >= 3) {
          imageView.setImage(
              new Image(
                  "file:DATABASE/autosaves/"
                      + scoreListSorted.get(2).split("[0-9]")[0].strip()
                      + ".png"));
          imageDisplayed = 2;
          imageDescriptorLabel.setText(
              "Your third best drawing: " + scoreListSorted.get(2).split("[0-9]")[0].strip());
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
        imageView.setImage(
            new Image(
                "file:DATABASE/autosaves/"
                    + scoreListSorted.get(0).split("[0-9]")[0].strip()
                    + ".png"));
        imageDisplayed = 0;
        imageDescriptorLabel.setText(
            "Your best drawing: " + scoreListSorted.get(0).split("[0-9]")[0].strip());
        break;
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
