package nz.ac.auckland.se206.controllers;

public class Settings {

  private double accuracyLevel = 0.0;

  private double wordsLevel = 0.0;

  public void setAccuracyLevel(double level) {
    this.accuracyLevel = level;
  }

  public void setWordsLevel(double level) {
    this.wordsLevel = level;
  }

  public double getAccuracyLevel() {
    return this.accuracyLevel;
  }

  public double getWordsLevel() {
    return this.wordsLevel;
  }
}
