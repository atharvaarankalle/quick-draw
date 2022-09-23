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

public class statsManager {
    private static List<String> userStats;
    // private static Map<String, Integer> wordAndRecord = new HashMap<String,
    // Integer>();
    private static List<Score> records;
    private static int gameWon = 0;
    private static int gameLost = 0;
    private static int topScore = 60;
    private static String topWord = null;

    public static ArrayList<String> getUserList() throws IOException {
        Path userIDPath = Paths.get("DATABASE/UserDatas.txt");
        List<String> allUserID = Files.readAllLines(userIDPath);
        ArrayList<String> userList = new ArrayList<String>();
        for (String id : allUserID) {
            if (!userList.contains(id)) {
                userList.add(id);
            }
        }
        return userList;
    }

    public static void readUserStats(String currentID) throws IOException {
        // Set up path and start reading user stats, save stats line by line into a list
        Path userStatsPath = Paths.get("DATABASE/" + currentID);
        userStats = Files.readAllLines(userStatsPath);
        topWord = null;
        gameLost=0;
        gameWon=0;
        manageStats(currentID);
    }

    public static void manageStats(String currentID) {
        records = new ArrayList<Score>();
        int timetaken;
        String[] seperatedStats;
        Map<String, Integer> wordAndRecord = new HashMap<String, Integer>();
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
                if (wordAndRecord.containsKey(seperatedStats[0])){
                    if(wordAndRecord.get(seperatedStats[0]) > timetaken) {
                        wordAndRecord.replace(seperatedStats[0], timetaken);
                    }
                } else {
                    wordAndRecord.put(seperatedStats[0], timetaken);
                }
            } else {
                gameLost++;
            }
        }

        for (String word : wordAndRecord.keySet()) {
            records.add(new Score(word, wordAndRecord.get(word), currentID));
        }
        Collections.sort(records);
    }
    /**
     * Differ from get records, which returns all record of the current user
     * This method returns the record corresponding to a specific word of the current user if he/she has it
     * Return null if the user doesn't have that record
     * @param word the specific word
     * @return
     */
    public static Score getRecord(String word){
        for(Score record : records){
            if(record.getWord().equals(word)){
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
    for(String id : allUsers){
        try{
        readUserStats(id);
        Score score = getRecord(word);
        if(score!=null){
            allScores.add(score);
        }
        } catch (IOException e){
        }
    }
    Collections.sort(allScores);
    return allScores;
    }

    public static int getNumberOfGames() {
        return userStats.size();
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
}
