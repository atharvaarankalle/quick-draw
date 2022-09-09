package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
    @FXML private Label userIDLable;

    @FXML private Label totalGamesLable;

    @FXML private Label gamesWonLable;

    @FXML private Label gamesLostLable;

    @FXML private Label bestRecordLable;

    @FXML private Button menuButton;

    @FXML private Label noStatsLabel;

    @FXML private ListView<String> scoreList;

    @FXML private AnchorPane backgroundPane;

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
                    scoreList.getItems().add(seperatedStats[0] + "  " + timetaken + " seconds");
                } else {
                    gameLost++;
                }
            }
            // After the scan, update all information
            gamesWonLable.setText(String.valueOf(gameWon));
            gamesLostLable.setText(String.valueOf(gameLost));
            if (topWord != null) {
                bestRecordLable
                        .setText("Wow! Drawing a " + topWord + " only took you " + String.valueOf(topScore)
                                + " seconds!");
            } else {
                bestRecordLable.setText("Oops, seems like you haven't won any games yet, don't give up!");
            }
        } catch (IOException e) {
            ObservableList<Node> allNodes= backgroundPane.getChildren();
            for(Node node : allNodes){
                node.setVisible(false);
            }
            noStatsLabel.setVisible(true);
            menuButton.setVisible(true);
        }
    }
}
