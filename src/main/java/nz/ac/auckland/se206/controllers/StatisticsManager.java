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
  private static String previousUserID = "";

  /**
   * This method gets all the users that have registered and compiles their usernames into a list
   *
   * @return A list of usernames
   * @throws IOException
   */
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

  /**
   * This method initializes the variables needed to start reading and updating the user statistics
   * on the scoreboard page
   *
   * @param currentID The username of the current user logged in
   * @throws IOException
   */
  public static void readUserStatistics(String currentID) throws IOException {
    // Set up path and start reading user stats, save stats line by line into a list
    Path userStatsPath = Paths.get("DATABASE/" + currentID);
    userStats = Files.readAllLines(userStatsPath);

    // Initialise the statistics variables and call the updateUserStatistics() method
    topWord = null;
    gameLost = 0;
    gameWon = 0;
    updateUserStatistics(currentID);
  }

  /**
   * This method reads the game history for a user from the database and updates the user statistics
   * in the GUI
   *
   * @param currentID The username of the current user logged in
   */
  private static void updateUserStatistics(String currentID) {

    // Initialise variables needed for reading and recording user statistics
    Settings gameSettings = (Settings) gameStage.getUserData();
    records = new ArrayList<Score>();
    String[] seperatedStats;
    Map<String, Integer> wordAndRecord = new HashMap<String, Integer>();

    // If a different user has logged in, clear the statistics of the previous user
    if (!(previousUserID.equals(currentID))) {
      timesTaken.clear();
      seenWords.clear();
      previousUserID = currentID;
    }

    // Read the user statistics file line-by-line, splitting the line based on " , "
    for (int i = 0; i < userStats.size(); i++) {
      seperatedStats = userStats.get(i).split(" , ");

      // If the recorded game has been won, calculate the time taken and add the word to the seen
      // list
      if (seperatedStats[1].equals("WON")) {
        gameWon++;

        // Calculate the time taken:
        if (!seenWords.contains(seperatedStats[0])) {
          timesTaken.add(
              Integer.valueOf(seperatedStats[3])
                  - Integer.valueOf(seperatedStats[2].split(" ")[0]));

          // If the user has broken their record, set the current statistic as the new record
          if (timesTaken.get(i) <= topScore && timesTaken.get(i) >= 0) {
            topScore = timesTaken.get(i);
            topWord = seperatedStats[0];
          }
          seenWords.add(seperatedStats[0]);
        }

        // Update the current best word and time if a record has been broken.
        if (wordAndRecord.containsKey(seperatedStats[0])) {
          if (wordAndRecord.get(seperatedStats[0]) > timesTaken.get(i) && timesTaken.get(i) >= 0) {
            wordAndRecord.replace(seperatedStats[0], timesTaken.get(i));
          }
        } else {
          wordAndRecord.put(seperatedStats[0], timesTaken.get(i));
        }
      } else if (seperatedStats[1].equals("LOST")) {
        // If the recorded game has been lost, record this as a loss and add -1 to the timesTaken
        // array
        wordAndRecord.put(seperatedStats[0], gameSettings.getTimeLevel() + 1);
        if (!seenWords.contains(seperatedStats[0])) {
          timesTaken.add(-1);
          seenWords.add(seperatedStats[0]);
        }
        gameLost++;
      }
    }

    // For each word played by the user, add the statistics to the list and sort it
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
   * @return a Score object containing the information about the recorded word
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
   * @return an ArrayList of Scores that are stored on the current leader board
   * @throws IOException
   */
  public static ArrayList<Score> getLeaderBoard(String word) throws IOException {
    // Initialise lists that are read from and written to
    ArrayList<Score> allScores = new ArrayList<Score>();
    List<String> allUsers;
    allUsers = getUserList();
    for (String id : allUsers) { // Iterates all the users to find specific word exist
      try {
        readUserStatistics(id);
        Score score = getRecord(word);

        // If a record of the current word exists, add it to the list of all scores
        if (score != null) {
          allScores.add(score);
        }
      } catch (IOException ioException) {
        // To catch any exceptions
      }
    }

    // Sort the list of all scores and return this
    Collections.sort(allScores);
    return allScores;
  }

  /**
   * This method returns the number of games played by the currently logged in user
   *
   * @return The number of games played by the user as type Integer
   */
  public static int getNumberOfGames() {
    return userStats.size();
  }

  /**
   * This method returns the number of games won by the currently logged in user
   *
   * @return The number of games won by the user as type Integer
   */
  public static int getGameWon() {
    return gameWon;
  }

  /**
   * This method returns the number of games lost by the currently logged in user
   *
   * @return The number of games lost by the user as type Integer
   */
  public static int getGameLost() {
    return gameLost;
  }

  /**
   * This method returns the list of records of the user currently logged in
   *
   * @return The list of records for the current user, as type List<Score>
   */
  public static List<Score> getRecords() {
    return records;
  }

  /**
   * This method returns the best drawn word by the user currently logged in
   *
   * @return The best drawn word, as type String
   */
  public static String getTopWord() {
    return topWord;
  }

  /**
   * This method returns the top score of the user currently logged in
   *
   * @return The top score of the user, as type Integer
   */
  public static int getTopScore() {
    return topScore;
  }

  /**
   * This method sets the game stage so that the user data can be accessed
   *
   * @param stage The stage the current scene is located in
   */
  public static void setGameStage(Stage stage) {
    gameStage = stage;
  }
}
