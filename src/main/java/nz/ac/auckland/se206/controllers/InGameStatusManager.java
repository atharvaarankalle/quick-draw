package nz.ac.auckland.se206.controllers;

public class InGameStatusManager {
  /*
   * This class is used for detecting weather the game is currently in a game or not
   * when inGameStatus is true, it means the game is currently in running a time line for a game, vice versa
   */
  private static Boolean inGameStatus;
  /**
   * This method returns the ingame status
   *
   * @return a boolean showing whether a game is running.
   */
  public static Boolean isInGame() {
    return inGameStatus;
  }
  /**
   * This method sets the ingame status to desired boolean value
   *
   * @param status the boolean value of the new ingame status
   */
  public static void setInGameStatus(Boolean status) {
    inGameStatus = status;
  }
}
