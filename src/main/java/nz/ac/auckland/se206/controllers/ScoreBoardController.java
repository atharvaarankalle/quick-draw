package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
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

    private Map<String, Integer> wordAndRecord = new HashMap<String, Integer>();

    public void initialize() {
        try {
            noStatsLabel.setVisible(false);
            // First read the current user id
            Path userDataPath = Paths.get("UserDatas.txt");
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
            // Set up path and start reading user stats, save stats line by line into a list
            Path userStatsPath = Paths.get(currentID);
            List<String> userStats = Files.readAllLines(userStatsPath);
            totalGamesLable.setText(String.valueOf(userStats.size()));
            // Start scanning through the user stats
            int gameWon = 0;
            int gameLost = 0;
            int topScore = 60;
            String topWord = null;
            int timetaken;
            String[] seperatedStats;
            for (String line : userStats) {
                seperatedStats = line.split(" , ");
                if (seperatedStats[1].equals("WON")) {
                    gameWon++;
                    // Calculate the time taken:
                    timetaken = 60 - Integer.valueOf(seperatedStats[2].split(" ")[0]);
                    if (timetaken <= topScore) {
                        topScore = timetaken;
                        topWord = seperatedStats[0];
                    }
                    // If the player break his/her record
                    if (wordAndRecord.containsKey(seperatedStats[0])
                            && wordAndRecord.get(seperatedStats[0]) < timetaken) {
                        wordAndRecord.replace(seperatedStats[0], timetaken);
                    } else {
                        wordAndRecord.put(seperatedStats[0], timetaken);
                    }
                    scoreList.getItems().add(seperatedStats[0] + "  " + timetaken + " seconds");
                } else {
                    gameLost++;
                }
            }
            // After the scan, update all information
            gamesWonLable.setText(String.valueOf(gameWon));
            gamesLostLable.setText(String.valueOf(gameLost));
            if (topWord != null) {
                bestRecordWordLabel.setText(topWord + "!");
                bestRecordTimeLabel.setText(String.valueOf(topScore) + " seconds to draw one!");
            } else {
                textLabel1.setText("Oops, seems like you haven't won any games yet...");
                textLabel2.setText("But don't give up! Let's try again!");
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
}
