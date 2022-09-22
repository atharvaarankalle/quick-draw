package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class statsManager {
    private static List<String> userStats;
    private static Map<String, Integer> wordAndRecord = new HashMap<String, Integer>();
    private static int gameWon = 0;
    private static int gameLost = 0;
    private static int topScore = 60;
    private static String topWord = null;

    public static void readUserStats(String currentID) throws IOException {
        // Set up path and start reading user stats, save stats line by line into a list
        Path userStatsPath = Paths.get("DATABASE/" + currentID);
        userStats = Files.readAllLines(userStatsPath);
    }

    public static void manageStats() {
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
        } else {
          gameLost++;
        }
      }
    }

    public static int getNumberOfGames(){
        return userStats.size();
    }

    public static int getGameWon(){
        return gameWon;
    }

    public static int getGameLost(){
        return gameLost;
    }

    public static Map<String, Integer> getWordAndRecord(){
        return wordAndRecord;
    }

    public static String getTopWord(){
        return topWord;
    }

    public static int getTopScore(){
        return topScore;
    }
}
