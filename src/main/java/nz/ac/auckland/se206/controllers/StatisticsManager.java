package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;

public class StatisticsManager {
  private static List<String> userStats;
  private static List<Score> records;
  private static int gameWon = 0;
  private static int gameLost = 0;
  private static int topScore = 60;
  private static String topWord = null;
  private static Stage gameStage;
  private static List<String> seenWords = new ArrayList<String>();
  private static List<Integer> timesTaken = new ArrayList<Integer>();

  public static ArrayList<String> getUserList() throws IOException {
    // Collects all the user information from specific stored
    // "DATABASE/UserDatas.txt"
    Path userNamePath = Paths.get("DATABASE/UserDatas.txt");
    List<String> allUserName = Files.readAllLines(userNamePath);
    ArrayList<String> userList = new ArrayList<String>();
    for (String id : allUserName) { // Checking if the specific user id exist inside userlist
      if (!userList.contains(id)) {
        userList.add(id);
      }
    }
    return userList;
  }

  public static void readUserStatistics(String currentID) throws IOException {
    // Set up path and start reading user stats, save stats line by line into a list
    Path userStatsPath = Paths.get("DATABASE/" + currentID);
    userStats = Files.readAllLines(userStatsPath);
    topWord = null;
    gameLost = 0;
    gameWon = 0;
    updateUserStatistics(currentID);
  }

  private static void updateUserStatistics(String currentID) {

    Settings gameSettings = (Settings) gameStage.getUserData();
    // Updating the statistics inside stats fxml file, for each users
    records = new ArrayList<Score>();
    String[] seperatedStats;
    Map<String, Integer> wordAndRecord = new HashMap<String, Integer>();
    for (int i = 1; i < userStats.size(); i++) {
      seperatedStats = userStats.get(i).split(" , ");
      if (seperatedStats[1].equals("WON")) {
        gameWon++;
        // Calculate the time taken:
        if (!seenWords.contains(seperatedStats[0])) {
          timesTaken.add(
              gameSettings.getTimeLevel() - Integer.valueOf(seperatedStats[2].split(" ")[0]));
          if (timesTaken.get(i) <= topScore && timesTaken.get(i) >= 0) {
            topScore = timesTaken.get(i);
            topWord = seperatedStats[0];
          }
          seenWords.add(seperatedStats[0]);
        }
        // If the player break his/her record
        if (wordAndRecord.containsKey(seperatedStats[0])) {
          if (wordAndRecord.get(seperatedStats[0]) > timesTaken.get(i) && timesTaken.get(i) >= 0) {
            wordAndRecord.replace(seperatedStats[0], timesTaken.get(i));
          }
        } else {
          wordAndRecord.put(seperatedStats[0], timesTaken.get(i));
        }
      } else {
        if (!wordAndRecord.containsKey(seperatedStats[0])) {
          wordAndRecord.put(seperatedStats[0], gameSettings.getTimeLevel() + 1);
          timesTaken.add(-1);
        }
        gameLost++;
      }
    }

    for (String word : wordAndRecord.keySet()) {
      records.add(new Score(word, wordAndRecord.get(word), currentID));
    }
    Collections.sort(records);
  }

  /**
   * Differ from get records, which returns all record of the current user This method returns the
   * record corresponding to a specific word of the current user if he/she has it Return null if the
   * user doesn't have that record
   *
   * @param word the specific word
   * @return
   */
  public static Score getRecord(String word) {
    for (Score record : records) {
      if (record.getWord().equals(word)) {
        return record;
      }
    }
    return null;
  }

  /**
   * Get the list of scores of all users that have drawn the specific word
   *
   * @param word the specific word
   * @return
   * @throws IOException
   */
  public static ArrayList<Score> getLeaderBoard(String word) throws IOException {
    ArrayList<Score> allScores = new ArrayList<Score>();
    List<String> allUsers;
    allUsers = getUserList();
    for (String id : allUsers) { // Iterates all the users to find specific word exist
      try {
        readUserStatistics(id);
        Score score = getRecord(word);
        if (score != null) {
          allScores.add(score);
        }
      } catch (IOException ioException) {
        // To catch any exceptions
      }
    }
    Collections.sort(allScores);
    return allScores;
  }

  public static int getNumberOfGames() {
    return userStats.size() - 1;
  }

  public static int getGameWon() {
    return gameWon;
  }

  public static int getGameLost() {
    return gameLost;
  }

  public static List<Score> getRecords() {
    return records;
  }

  public static String getTopWord() {
    return topWord;
  }

  public static int getTopScore() {
    return topScore;
  }

  public static void setGameStage(Stage stage) {
    gameStage = stage;
  }
}
