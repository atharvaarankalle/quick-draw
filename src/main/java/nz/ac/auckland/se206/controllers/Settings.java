package nz.ac.auckland.se206.controllers;

public class Settings {

  private double accuracyLevel = 0.0;

  private double wordsLevel = 0.0;

  private double timeLevel = 0.0;

  private double confidenceLevel = 0.0;

  private String currentUser = "";

  public void setAccuracyLevel(double level) {
    this.accuracyLevel = level;
  }

  public void setWordsLevel(double level) {
    this.wordsLevel = level;
  }

  public void setTimeLevel(double level) {
    this.timeLevel = level;
  }

  public void setConfidenceLevel(double level) {
    this.confidenceLevel = level;
  }

  public void setCurrentUser(String username) {
    this.currentUser = username;
  }

  public double getAccuracyLevel() {
    return this.accuracyLevel;
  }

  public double getWordsLevel() {
    return this.wordsLevel;
  }

  public int getTimeLevel() {
    switch ((int) this.timeLevel) {
      case 0:
        return 60;
      case 1:
        return 45;
      case 2:
        return 30;
      case 3:
        return 15;
      default:
        return 60;
    }
  }

  public double getPredictionLevel() {
    switch ((int) this.confidenceLevel) {
      case 0:
        return 0.01;
      case 1:
        return 0.1;
      case 2:
        return 0.25;
      case 3:
        return 0.50;
      default:
        return 0.01;
    }
  }

  public double getTimeSliderPosition() {
    return this.timeLevel;
  }

  public double getConfidenceSliderPosition() {
    return this.confidenceLevel;
  }

  public String getCurrentUser() {
    return this.currentUser;
  }
}
