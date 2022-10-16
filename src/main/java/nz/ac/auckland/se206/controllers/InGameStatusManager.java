package nz.ac.auckland.se206.controllers;

public class InGameStatusManager {
    /*
     * This class is used for detecting weather the game is currently in a game or not
     * when inGameStatus is true, it means the game is currently in running a time line for a game, vice versa
     */
    private static Boolean inGameStatus;

    public static Boolean isInGame() {
        return inGameStatus;
    }

    public static void setInGameStatus(Boolean status){
        inGameStatus = status;
    }
}
