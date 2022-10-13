package nz.ac.auckland.se206.controllers;

public class Settings {

  private double accuracyLevel = 0.0;

  private double wordsLevel = 0.0;

  private double timeLevel = 0.0;

  private double confidenceLevel = 0.0;

  private double sfxVolume = 75.0;

  private double bgmVolume = 75.0;

  private int muteStatus = 0;

  private String currentUser = "";

  /**
   * Sets the accuracy level for the current user that is logged in
   *
   * @param level The accuracy level to set.
   */
  public void setAccuracyLevel(double level) {
    this.accuracyLevel = level;
  }

  /**
   * Sets the words level for the current user that is logged in
   *
   * @param level The words level to set.
   */
  public void setWordsLevel(double level) {
    this.wordsLevel = level;
  }

  /**
   * Sets the time level for the current user that is logged in
   *
   * @param level The time level to set.
   */
  public void setTimeLevel(double level) {
    this.timeLevel = level;
  }

  /**
   * Sets the confidence level for the current user that is logged in
   *
   * @param level The confidence level to set.
   */
  public void setConfidenceLevel(double level) {
    this.confidenceLevel = level;
  }
  /**
   * Sets the current user that is logged in
   *
   * @param user The user to set.
   */
  public void setSfxVolume(double sfxVolume) {
      this.sfxVolume = sfxVolume;
  }

  public void setBgmVolume(double bgmVolume) {
      this.bgmVolume = bgmVolume;
  }

  public void setMuteStatus(int muteStatus) {
      this.muteStatus = muteStatus;
  }
  public void setCurrentUser(String username) {
    this.currentUser = username;
  }

  /**
   * Returns the accuracy level for the current user that is logged in
   *
   * @return The accuracy level.
   */
  public double getAccuracyLevel() {
    return this.accuracyLevel;
  }

  /**
   * Returns the words level for the current user that is logged in
   *
   * @return The words level.
   */
  public double getWordsLevel() {
    return this.wordsLevel;
  }

  /**
   * Returns the time level for the current user that is logged in
   *
   * @return The time level.
   */
  public int getTimeLevel() {
    // Switch case to convert the time level to an integer value in seconds
    switch ((int) this.timeLevel) {
      case 0:
        // Easy level
        return 60;
      case 1:
        // Medium level
        return 45;
      case 2:
        // Hard level
        return 30;
      case 3:
        // Master level
        return 15;
      default:
        return 60;
    }
  }

  /**
   * Returns the confidence level for the current user that is logged in
   *
   * @return The confidence level.
   */
  public double getPredictionLevel() {
    // Switch case to convert the confidence level to a double value, the level
    switch ((int) this.confidenceLevel) {
      case 0:
        // Easy level
        return 0.01;
      case 1:
        // Medium level
        return 0.1;
      case 2:
        // Hard level
        return 0.25;
      case 3:
        // Master level
        return 0.50;
      default:
        return 0.01;
    }
  }

  /**
   * Get the current time slider position for the current user that is logged in
   *
   * @return The time slider position
   */
  public double getSfxVolume() {
      return sfxVolume;
  }

  public double getBgmVolume() {
      return bgmVolume;
  }

public int getMuteStatus() {
    return muteStatus;
}

  public double getTimeSliderPosition() {
    return this.timeLevel;
  }

  /**
   * Get the current confidence slider position for the current user that is logged in
   *
   * @return The confidence slider position
   */
  public double getConfidenceSliderPosition() {
    return this.confidenceLevel;
  }

  /**
   * Get the current username of the user that is logged in
   *
   * @return The current user's username
   */
  public String getCurrentUser() {
    return this.currentUser;
  }
}
