package nz.ac.auckland.se206.controllers;

public class Settings {

  private double accuracyLevel = 0.0;

  private double wordsLevel = 0.0;

  private double timeLevel = 0.0;

  public void setAccuracyLevel(double level) {
    this.accuracyLevel = level;
  }

  public void setWordsLevel(double level) {
    this.wordsLevel = level;
  }

  public void setTimeLevel(double level) {
    this.timeLevel = level;
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

  public double getTimeSliderPosition() {
    return this.timeLevel;
  }
}
